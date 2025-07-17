package com.githubscanner.nonfork_finder.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubRepositoryRaw {
    private String name;
    private boolean fork;
    private Owner owner;
}
