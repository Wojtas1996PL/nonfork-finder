package com.githubscanner.nonfork_finder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.githubscanner.nonfork_finder.client.GitHubClient;
import com.githubscanner.nonfork_finder.dto.BranchDto;
import com.githubscanner.nonfork_finder.dto.GitHubResponseDto;
import com.githubscanner.nonfork_finder.model.Commit;
import com.githubscanner.nonfork_finder.model.GitHubRepositoryRaw;
import com.githubscanner.nonfork_finder.model.Owner;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Import(GitHubServiceTest.TestConfig.class)
class GitHubServiceTest {
    @Autowired
    private GitHubService gitHubService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GitHubClient gitHubClient() {
            return new GitHubClient(new RestTemplate()) {
                @Override
                public List<GitHubRepositoryRaw> getAllRepositories(String username) {
                    Owner owner = new Owner();
                    owner.setLogin("user");

                    GitHubRepositoryRaw repository1 = new GitHubRepositoryRaw();
                    repository1.setName("repository1");
                    repository1.setFork(true);
                    repository1.setOwner(owner);

                    GitHubRepositoryRaw repository2 = new GitHubRepositoryRaw();
                    repository2.setName("repository2");
                    repository2.setFork(false);
                    repository2.setOwner(owner);

                    return List.of(repository1, repository2);
                }

                @Override
                public List<BranchDto> getAllBranches(String ownerLogin, String repositoryName) {
                    Commit commit1 = new Commit();
                    commit1.setSha("qwerty123");
                    BranchDto branch1 = new BranchDto("main", commit1);

                    Commit commit2 = new Commit();
                    commit2.setSha("xyz95");
                    BranchDto branch2 = new BranchDto("develop", commit2);

                    return List.of(branch1, branch2);
                }
            };
        }
    }

    @Test
    public void returnsNonForkedRepositoriesWithBranchesForValidUser() {
        String username = "user";

        List<GitHubResponseDto> result = gitHubService.getNonForkedRepositoriesByUsername(username);

        assertEquals(1, result.size());

        GitHubResponseDto repo = result.getFirst();
        assertEquals("repository2", repo.name());
        assertEquals("user", repo.ownerLogin());
        assertEquals(2, repo.branches().size());

        BranchDto mainBranch = repo.branches().getFirst();
        assertEquals("main", mainBranch.name());
        assertEquals("qwerty123", mainBranch.lastCommitSha());

        BranchDto devBranch = repo.branches().get(1);
        assertEquals("develop", devBranch.name());
        assertEquals("xyz95", devBranch.lastCommitSha());
    }
}
