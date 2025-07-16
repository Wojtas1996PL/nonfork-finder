package com.githubscanner.nonfork_finder.client;

import com.githubscanner.nonfork_finder.dto.BranchDto;
import com.githubscanner.nonfork_finder.exception.EmptyGitHubResponseException;
import com.githubscanner.nonfork_finder.exception.UserNotFoundException;
import com.githubscanner.nonfork_finder.model.GitHubRepositoryRaw;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class GitHubClient {
    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private final RestTemplate restTemplate;

    public List<GitHubRepositoryRaw> getAllRepositories(String username) {
        String url = GITHUB_BASE_URL + "/users/" + username + "/repos";
        try {
            ResponseEntity<GitHubRepositoryRaw[]> response =
                    restTemplate.getForEntity(url, GitHubRepositoryRaw[].class);
            if (response.getBody() == null) {
                throw new EmptyGitHubResponseException("Response body is empty");
            }
            return List.of(response.getBody());
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("GitHub user not found");
            }
            throw new RuntimeException("GitHub API error: " + e.getMessage(), e);
        }
    }

    public List<BranchDto> getAllBranches(String ownerLogin, String repositoryName) {
        String url = GITHUB_BASE_URL + "/repos/" + ownerLogin + "/" + repositoryName + "/branches";
        ResponseEntity<BranchDto[]> response = restTemplate.getForEntity(url, BranchDto[].class);
        if (response.getBody() == null) {
            throw new EmptyGitHubResponseException("Response body is empty");
        }
        return List.of(response.getBody());
    }
}
