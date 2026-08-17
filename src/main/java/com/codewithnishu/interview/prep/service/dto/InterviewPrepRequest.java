package com.codewithnishu.interview.prep.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewPrepRequest {
    @NotBlank
    private  String jobDescription;
    @NotBlank
    private String companyName;
    @NotBlank
    private String jobTitle;
    private Long applicationId;
}
