package com.githubscanner.nonfork_finder.service;

import com.githubscanner.nonfork_finder.dto.RepositoryDto;
import java.util.List;

public interface GitHubService {
    List<RepositoryDto> getNonForkedRepositories(String username);
}
