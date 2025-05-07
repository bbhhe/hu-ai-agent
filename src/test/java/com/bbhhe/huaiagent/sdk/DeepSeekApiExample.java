package com.bbhhe.huaiagent.sdk;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class DeepSeekApiExample {
    public static void main(String[] args) {
        // 设置请求URL
        String url = "https://api.deepseek.com/chat/completions";

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "deepseek-chat");
        requestBody.set("messages", JSONUtil.parseArray("[{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"你好!\"}]"));
        requestBody.set("stream", false);

        // 发送POST请求
        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer sk-xxxx")
                .body(requestBody.toString())
                .execute();

        // 输出响应结果
        System.out.println(response.body());
    }
}

