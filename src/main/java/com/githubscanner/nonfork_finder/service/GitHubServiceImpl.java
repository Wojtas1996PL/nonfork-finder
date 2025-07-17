package com.githubscanner.nonfork_finder.service;

import com.githubscanner.nonfork_finder.client.GitHubApiClient;
import com.githubscanner.nonfork_finder.dto.BranchDto;
import com.githubscanner.nonfork_finder.dto.GitHubResponseDto;
import com.githubscanner.nonfork_finder.model.GitHubRepositoryRaw;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GitHubServiceImpl implements GitHubService {
    private final GitHubApiClient gitHubApiClient;

    @Override
    public List<GitHubResponseDto> getNonForkedRepositoriesByUsername(String username) {
        List<GitHubRepositoryRaw> rawRepositories = gitHubApiClient.getAllRepositories(username);
        return rawRepositories.stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> {
                    List<BranchDto> branchDtos = gitHubApiClient.getAllBranches(
                            repo.getOwner().getLogin(),
                            repo.getName()
                    );

                    return new GitHubResponseDto(
                            repo.getName(),
                            repo.getOwner().getLogin(),
                            branchDtos
                    );
                })
                .collect(Collectors.toList());
    }
}
