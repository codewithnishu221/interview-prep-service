package com.codewithnishu.interview.prep.service.dto;

import com.codewithnishu.interview.prep.service.enums.QuestionCategory;
import com.codewithnishu.interview.prep.service.enums.QuestionDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewQuestion {
    private String question;
    private QuestionCategory category;
    private QuestionDifficulty difficulty;
    private String hint;
}
