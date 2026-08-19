package com.codewithnishu.interview.prep.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TavilySearchResult {
   private String title;
   private String url;
   private String content;
   private Double score;
}
