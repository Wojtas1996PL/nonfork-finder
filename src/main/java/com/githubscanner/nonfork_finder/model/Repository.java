package com.githubscanner.nonfork_finder.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Repository {
    private String name;
    private String ownerLogin;
    private List<Branch> branches;
}
