package com.codewithnishu.interview.prep.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionsLlmResponse {
    private List<InterviewQuestion> technicalQuestions;
    private List<InterviewQuestion> behavioralQuestions;
    private List<InterviewQuestion> systemDesignQuestions;
}
