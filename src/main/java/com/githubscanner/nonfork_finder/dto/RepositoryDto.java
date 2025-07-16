package com.githubscanner.nonfork_finder.dto;

import java.util.List;

public record RepositoryDto(String name, String ownerLogin, List<BranchDto> branches) {
}
