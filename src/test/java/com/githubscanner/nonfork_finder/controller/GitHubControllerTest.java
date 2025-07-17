package com.githubscanner.nonfork_finder.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.githubscanner.nonfork_finder.client.GitHubApiClient;
import com.githubscanner.nonfork_finder.dto.BranchDto;
import com.githubscanner.nonfork_finder.model.GitHubRepositoryRaw;
import com.githubscanner.nonfork_finder.model.Owner;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GitHubControllerIntegrationTest.TestConfig.class)
class GitHubControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsValidRepositoriesWithBranches() throws Exception {
        mockMvc.perform(get("/users/user/repos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("repository2"))
                .andExpect(jsonPath("$[0].ownerLogin").value("user"))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("qwerty123"))
                .andExpect(jsonPath("$[0].branches[1].name").value("develop"))
                .andExpect(jsonPath("$[0].branches[1].lastCommitSha").value("xyz95"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GitHubApiClient gitHubApiClient() {
            return new GitHubApiClient(new RestTemplate()) {
                @Override
                public List<GitHubRepositoryRaw> getAllRepositories(String username) {
                    Owner owner = new Owner();
                    owner.setLogin("user");

                    GitHubRepositoryRaw repo1 = new GitHubRepositoryRaw();
                    repo1.setName("repository1");
                    repo1.setFork(true);
                    repo1.setOwner(owner);

                    GitHubRepositoryRaw repo2 = new GitHubRepositoryRaw();
                    repo2.setName("repository2");
                    repo2.setFork(false);
                    repo2.setOwner(owner);

                    return List.of(repo1, repo2);
                }

                @Override
                public List<BranchDto> getAllBranches(String ownerLogin, String repoName) {
                    BranchDto main = new BranchDto();
                    main.setName("main");
                    main.setCommit(Map.of("sha", "qwerty123"));

                    BranchDto dev = new BranchDto();
                    dev.setName("develop");
                    dev.setCommit(Map.of("sha", "xyz95"));

                    return List.of(main, dev);
                }
            };
        }
    }
}

