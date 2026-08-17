package com.codewithnishu.interview.prep.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayPlan {

    private int dayNumber;
    private String focus;
    private List<String> tasks;
}
