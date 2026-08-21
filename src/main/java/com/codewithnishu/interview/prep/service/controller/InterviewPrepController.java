package com.codewithnishu.interview.prep.service.controller;

import com.codewithnishu.interview.prep.service.dto.InterviewPrepRequest;
import com.codewithnishu.interview.prep.service.dto.InterviewPrepResponse;
import com.codewithnishu.interview.prep.service.service.InterviewPrepService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/interview-prep")
@RequiredArgsConstructor
public class InterviewPrepController {

    private final InterviewPrepService interviewPrepService;
    @PostMapping("/generate")
    public ResponseEntity<InterviewPrepResponse> generatePrep(@RequestBody @Valid InterviewPrepRequest interviewPrepRequest, HttpServletRequest request){
        String authToken = request.getHeader("Authorization");
        InterviewPrepResponse response = interviewPrepService.generateInterviewPrep(interviewPrepRequest, authToken);
        return ResponseEntity.ok(response);
    }

}
