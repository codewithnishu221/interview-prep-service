package com.codewithnishu.interview.prep.service.service.Impl;

import com.codewithnishu.interview.prep.service.client.TavilyClient;
import com.codewithnishu.interview.prep.service.dto.*;
import com.codewithnishu.interview.prep.service.service.InterviewPrepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewPrepServiceImpl implements InterviewPrepService {

    private final OllamaChatModel chatModel;
    private final TavilyClient tavilyClient;
    private final ObjectMapper objectMapper;
    @Value("${app.generation.temperature}")
    private double temperature;


    @Override
    public InterviewPrepResponse generateInterviewPrep(InterviewPrepRequest request, String authToken) {
       List<InterviewResource> resources = tavilyClient.searchInterviewExperiences(
               request.getCompanyName(), request.getJobTitle()
       );
       String webContext = resources.stream()
               .map(r->r.getTitle() + ": " + r.getExcerpt())
               .collect(Collectors.joining("\n"));
       QuestionsLlmResponse questionsLlmResponse = generateQuestions(request, webContext);

       PrepPlanLlmResponse prepPlanLlmResponse = generatePrepPlan(request);
       InterviewPrepResponse response = new InterviewPrepResponse();
       response.setCompanyName(request.getCompanyName());
       response.setJobTitle(request.getJobTitle());
       response.setResources(resources);
       if(questionsLlmResponse!= null){
           response.setTechnicalQuestions(questionsLlmResponse.getTechnicalQuestions());
           response.setBehavioralQuestions(questionsLlmResponse.getBehavioralQuestions());
           response.setSystemDesignQuestions(questionsLlmResponse.getSystemDesignQuestions());
         } else {
           response.setTechnicalQuestions(List.of());
           response.setBehavioralQuestions(List.of());
           response.setSystemDesignQuestions(List.of());
       }
       if(prepPlanLlmResponse != null){
           response.setPrepPlan(prepPlanLlmResponse.getPrepPlan());
       } else {
           response.setPrepPlan(List.of());
       }
       response.setGeneratedAt(LocalDateTime.now());
        return response;
    }
    private QuestionsLlmResponse generateQuestions(InterviewPrepRequest request, String webContext){
        String jsonFormat = """
                {
                   "technicalQuestions": [
                       { "question": "...", "category": "TECHNICAL", "difficulty": "MEDIUM", "hint": "..." }
                   ],
                   "behavioralQuestions": [      
                       { "question": "...", "category": "BEHAVIORAL", "difficulty": "EASY", "hint": "..." }
                   ],
                   "systemDesignQuestions": [
                       { "question": "...", "category": "SYSTEM_DESIGN", "difficulty": "HARD", "hint": "..." }
                   ]
                }
                """;

        String template = """
                You are a senior technical interviewer hiring for a {jobTitle} role at {companyName}.
                JOB DESCRIPTION:
                {jobDescription}
                
                REAL INTERVIEW EXPERIENCES FROM THE WEB (Context):
                {webContext}
                
                Generate exactly 15 interview questions:
                            - 7 TECHNICAL questions tailored to the JD requirements
                            - 5 BEHAVIORAL questions relevant to the role
                            - 3 SYSTEM_DESIGN questions suitable for this position
                Respond ONLY with a valid JSON object in this exact schema, without markdown formatting:
                {format}
                """;

        Prompt prompt = new PromptTemplate(template).create(Map.of(
                "jobTitle", request.getJobTitle(),
                "companyName", request.getCompanyName(),
                "jobDescription", request.getJobDescription(), // Fixed bug here
                "webContext", webContext.isBlank() ? "No external context available." : webContext,
                "format", jsonFormat // Inject the JSON schema here
        ));

        try {
            String rawResponse = chatModel.call(prompt).getResult().getOutput().getText();
            return parseLlmResponse(rawResponse, QuestionsLlmResponse.class);
        } catch (Exception e) {
            log.error("Error generating interview questions from LLM: {}", e.getMessage(), e);
            return null;
        }
    }

    private PrepPlanLlmResponse generatePrepPlan(InterviewPrepRequest request){
        String jsonFormat = """
                {
                   "prepPlan": [
                       {
                           "dayNumber" : 1,
                           "focus": "Core Fundamentals & Concurrency",
                           "tasks" : [
                               "Review thread pools and executor framework",
                               "Practice 2 Leetcode medium questions"
                           ]
                       }
                   ]
                }
                """;

        String template = """
                You are an expert career coach helping a candidate prepare for a {jobTitle} role at {companyName}.
                
                JOB DESCRIPTION:
                {jobDescription}
                
                Create a focused, structured 7-day interview preparation plan.
                Each day must have a specific focus area and 3-4 actionable tasks.
                
                Respond ONLY with a valid JSON object in this exact schema:
                {format}
                """;

        Prompt prompt = new PromptTemplate(template).create(Map.of(
                "jobTitle", request.getJobTitle(),
                "companyName", request.getCompanyName(),
                "jobDescription", request.getJobDescription(),
                "format", jsonFormat // Inject the JSON schema here
        ));

        try {
            String rawResponse = chatModel.call(prompt).getResult().getOutput().getText();
            return parseLlmResponse(rawResponse, PrepPlanLlmResponse.class);
        } catch (Exception e) {
            log.error("Error generating prep plan from LLM: {}", e.getMessage(), e);
            return null;
        }
    }

    private <T> T parseLlmResponse(String rawResponse, Class<T> targetClass){
        try {
            if(rawResponse == null || rawResponse.isBlank()) return null;
           int startIndex = rawResponse.indexOf('{');
           int endIndex = rawResponse.lastIndexOf('}');
           if(startIndex != -1 && endIndex != -1 && startIndex <= endIndex){
               String jsonBlock = rawResponse.substring(startIndex, endIndex+1);
               return objectMapper.readValue(jsonBlock, targetClass);
           }
            log.warn("No JSON structure found in LLM response");
           return  null;
        } catch (Exception e) {
            log.error("Failed to parse LLM JSON for {}: {}", targetClass.getSimpleName(), e.getMessage());
            log.error("Raw LLM  Response was: \n{}", rawResponse);
            return null;
        }
    }
}
