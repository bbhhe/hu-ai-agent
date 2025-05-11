package com.bbhhe.huaiagent.app;

import com.bbhhe.huaiagent.advisor.ConversationContextAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class MyContextApp {

    private final ChatClient chatClient;

    public MyContextApp(ChatModel dashscopeChatModel) {
        ConversationContextAdvisor contextAdvisor = new ConversationContextAdvisor();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(
                        contextAdvisor
                )
                .build();
    }

    public String doChat(String userId, String message){
        ChatResponse chatResponse = chatClient.prompt()
                .advisors(a -> a.param("message", message).param("userId", userId))
                .user(message)
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",text);
        return text;
    }

}
