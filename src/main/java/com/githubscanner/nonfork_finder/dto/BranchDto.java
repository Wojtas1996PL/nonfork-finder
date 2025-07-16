package com.githubscanner.nonfork_finder.dto;

import com.githubscanner.nonfork_finder.model.Commit;

public record BranchDto(String name, Commit commit) {
    public String lastCommitSha() {
        return commit != null ? commit.getSha() : null;
    }
}
