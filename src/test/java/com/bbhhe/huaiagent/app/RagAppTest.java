package com.bbhhe.huaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RagAppTest {

    @Resource
    private RagApp ragApp;

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "SpringAi+本地知识库如何实现RAG？";
        String answer =  ragApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}
