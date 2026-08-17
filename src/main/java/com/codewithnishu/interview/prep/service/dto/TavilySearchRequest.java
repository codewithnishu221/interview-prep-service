package com.codewithnishu.interview.prep.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TavilySearchRequest {
    @JsonProperty("api_key")
    private String api_key;
    private String query;
    @JsonProperty("search_depth")
    private String search_depth;
    @JsonProperty("max_results")
    private int max_results;
    private List<String> include_domains;
}
