package com.bbhhe.huaiagent.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.Map;

public class WebSearchTool {

    // Tavily API 的搜索接口地址
    private static final String TAVILY_API_URL = "https://api.tavily.com/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information using Tavily Search API")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("query", query);
            requestBody.put("topic", "general");
            requestBody.put("search_depth", "basic");
            requestBody.put("chunks_per_source", 3);
            requestBody.put("max_results", 1);
            requestBody.put("days", 7);
            requestBody.put("include_answer", true);
            requestBody.put("include_raw_content", false);
            requestBody.put("include_images", false);
            requestBody.put("include_image_descriptions", false);
            requestBody.put("include_domains", new String[]{});
            requestBody.put("exclude_domains", new String[]{});

            // 发送POST请求
            HttpResponse response = HttpRequest.post(TAVILY_API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .execute();

            // 解析响应
            JSONObject jsonResponse = JSONUtil.parseObj(response.body());

            // 优先返回答案部分，如果没有再返回搜索结果
            if (jsonResponse.containsKey("answer")) {
                return jsonResponse.getStr("answer");
            } else if (jsonResponse.containsKey("results")) {
                JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);
                return firstResult.getStr("content");
            }

            return "No results found";
        } catch (Exception e) {
            return "Error searching with Tavily API: " + e.getMessage();
        }
    }
}
