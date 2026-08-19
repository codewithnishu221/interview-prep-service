package com.codewithnishu.interview.prep.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class PrepPlanLlmResponse {
    private List<DayPlan> prepPlan;
}
