package com.githubscanner.nonfork_finder.service;

import com.githubscanner.nonfork_finder.client.GitHubClient;
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
    private final GitHubClient gitHubClient;

    @Override
    public List<GitHubResponseDto> getNonForkedRepositoriesByUsername(String username) {
        List<GitHubRepositoryRaw> rawRepositories = gitHubClient.getAllRepositories(username);
        return rawRepositories.stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> {
                    List<BranchDto> branchDtos = gitHubClient.getAllBranches(
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
