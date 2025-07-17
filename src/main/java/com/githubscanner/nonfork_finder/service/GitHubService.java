package com.githubscanner.nonfork_finder.service;

import com.githubscanner.nonfork_finder.dto.GitHubResponseDto;
import java.util.List;

public interface GitHubService {
    List<GitHubResponseDto> getNonForkedRepositoriesByUsername(String username);
}
