package com.githubscanner.nonfork_finder.controller;

import com.githubscanner.nonfork_finder.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GitHubController {
    private final GitHubService gitHubService;

}
