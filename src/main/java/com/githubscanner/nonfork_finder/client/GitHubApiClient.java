package com.githubscanner.nonfork_finder.client;

import com.githubscanner.nonfork_finder.dto.BranchDto;
import com.githubscanner.nonfork_finder.exception.EmptyGitHubResponseException;
import com.githubscanner.nonfork_finder.exception.UserNotFoundException;
import com.githubscanner.nonfork_finder.model.GitHubRepositoryRaw;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GitHubApiClient {
    private final RestTemplate restTemplate;
    @Value("${github.base.url}")
    private String baseUrl;
    @Value("${github.token}")
    private String githubToken;

    public GitHubApiClient(RestTemplate restTemplate) {
        log.debug("Initializing GitHubApiClient...");
        this.restTemplate = restTemplate;
        log.debug("GitHubApiClient initialized with RestTemplate: {}", restTemplate);
    }

    public List<GitHubRepositoryRaw> getAllRepositories(String username) {
        String url = baseUrl + "/users/" + username + "/repos";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<GitHubRepositoryRaw[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GitHubRepositoryRaw[].class
            );
            if (response.getBody() == null) {
                throw new EmptyGitHubResponseException(HttpStatus.NO_CONTENT.value(), "Response body is empty");
            }
            return List.of(response.getBody());
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "GitHub user not found");
            }
            throw new RuntimeException("GitHub API error: " + e.getMessage(), e);
        }
    }

    public List<BranchDto> getAllBranches(String ownerLogin, String repositoryName) {
        String url = baseUrl + "/repos/" + ownerLogin + "/" + repositoryName + "/branches";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BranchDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                BranchDto[].class
        );
        if (response.getBody() == null) {
            throw new EmptyGitHubResponseException(HttpStatus.NO_CONTENT.value(), "Response body is empty");
        }
        return List.of(response.getBody());
    }
}
