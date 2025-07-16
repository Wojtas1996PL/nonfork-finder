package com.githubscanner.nonfork_finder.service;

import com.githubscanner.nonfork_finder.dto.RepositoryDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
class GitHubServiceImpl implements GitHubService {
    @Override
    public List<RepositoryDto> getNonForkedRepositoriesByUsername(String username) {
        return List.of();
    }
}
