package com.codewithnishu.interview.prep.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewResource {
    private String title;
    private String url;
    private String source;
    private String excerpt;
}
