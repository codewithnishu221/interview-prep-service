package com.codewithnishu.interview.prep.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TavilySearchResponse {
    private List<TavilySearchResult> results;
}
