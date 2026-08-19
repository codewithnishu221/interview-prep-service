package com.codewithnishu.interview.prep.service.service;

import com.codewithnishu.interview.prep.service.dto.InterviewPrepRequest;
import com.codewithnishu.interview.prep.service.dto.InterviewPrepResponse;

public interface InterviewPrepService {
    InterviewPrepResponse generateInterviewPrep(InterviewPrepRequest request, String authToken);
}
