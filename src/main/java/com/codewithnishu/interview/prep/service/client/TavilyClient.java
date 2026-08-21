package com.codewithnishu.interview.prep.service.client;

import com.codewithnishu.interview.prep.service.dto.InterviewResource;
import com.codewithnishu.interview.prep.service.dto.TavilySearchRequest;
import com.codewithnishu.interview.prep.service.dto.TavilySearchResponse;
import com.codewithnishu.interview.prep.service.dto.TavilySearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Component
@Slf4j
public class TavilyClient {

    private final String tavilyApiKey;
    private final RestClient restClient;
    public TavilyClient(@Qualifier("tavilyRestClient") RestClient restClient,
                        @Value("${app.tavily.api-key}") String tavilyApiKey){
        this.restClient = restClient;
        this.tavilyApiKey = tavilyApiKey;
    }

    public  List<InterviewResource> searchInterviewExperiences(String companyName, String jobTitle) {
        try {
            String query = companyName + " " + jobTitle + "interview experience questions 2024";
            List<String> domains = List.of("geeksforgeeks.org", "leetcode.com", "ambitionbox.com", "glassdoor.com", "interviewbit.com");
            TavilySearchRequest tavilySearchRequest = new TavilySearchRequest(
                    tavilyApiKey, query, "basic", 3, domains
            );

            TavilySearchResponse response = restClient.post()
                    .uri("/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(tavilySearchRequest)
                    .retrieve()
                    .body(TavilySearchResponse.class);
            if (response == null || response.getResults() == null) {
                return Collections.emptyList();
            }
            return response.getResults().stream()
                    .map(this::mapToInterviewResource)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch interview experiences from Tavily: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    private InterviewResource mapToInterviewResource(TavilySearchResult result){
        return InterviewResource.builder()
                .title(result.getTitle())
                .url(result.getUrl())
                .excerpt(result.getContent())
                .source(determineSource(result.getUrl()))
                .build();
    }
    private  String determineSource(String url){
        if(url ==null || url.isBlank()){
            return "Web";
        }
        String lowerUrl = url.toLowerCase();

        if(lowerUrl.contains("geeksforgeeks.org")){
            return "GeeksForGeeks";
        } else if (lowerUrl.contains("leetcode.com")) {
            return "LeetCode";
        } else if (lowerUrl.contains("ambitionbox.com")) {
            return "AmbitionBox";
        } else if (lowerUrl.contains("glassdoor.com")) {
            return "Glassdoor";
        } else if (lowerUrl.contains("interviewbit.com")) {
            return "InterviewBit";
        }
        return "Web";
    }

}
