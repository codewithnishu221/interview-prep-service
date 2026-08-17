package com.codewithnishu.interview.prep.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewPrepResponse {
    private String companyName;
    private String jobTitle;
   private List<InterviewQuestion> technicalQuestions;
   private List<InterviewQuestion> behavioralQuestions;
    private  List<InterviewQuestion> systemDesignQuestions;
    private List<InterviewResource> resources;
     private List<DayPlan> prepPlan;
     private LocalDateTime generatedAt;
}
