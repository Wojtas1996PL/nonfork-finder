package com.githubscanner.nonfork_finder.dto;

import java.util.List;

public record GitHubResponseDto(String name, String ownerLogin, List<BranchDto> branches) {
}
