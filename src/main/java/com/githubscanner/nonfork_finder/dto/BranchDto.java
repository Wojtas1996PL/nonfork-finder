package com.githubscanner.nonfork_finder.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchDto {
    private String name;
    private Map<String, Object> commit;

    @JsonProperty("commit")
    public void setCommit(Map<String, Object> commit) {
        this.commit = commit;
    }

    @JsonIgnore
    public Map<String, Object> getCommit() {
        return commit;
    }

    @JsonProperty("lastCommitSha")
    public String getLastCommitSha() {
        return commit != null ? (String) commit.get("sha") : null;
    }
}
