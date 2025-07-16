package com.githubscanner.nonfork_finder.controller;

import com.githubscanner.nonfork_finder.dto.ErrorResponse;
import com.githubscanner.nonfork_finder.dto.GitHubResponseDto;
import com.githubscanner.nonfork_finder.service.GitHubService;
import io.swagger.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class GitHubController {
    private final GitHubService gitHubService;

    @Operation(description = "Get repositories from user")
    @GetMapping("/{username}/repositories")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        try {
            List<GitHubResponseDto> repositories = gitHubService
                    .getNonForkedRepositoriesByUsername(username);
            return ResponseEntity.ok(repositories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse(404, e.getMessage()));
        }
    }
}
