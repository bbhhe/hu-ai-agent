package com.bbhhe.huaiagent.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MyContextAppTest {

    @Autowired
    private MyContextApp myContextApp;

    @Test
    public void testContextMaintenance() {
        String userId = "test-user-123";

        // 第一轮对话
        String answer = myContextApp.doChat(userId, "我喜欢足球");
        Assertions.assertNotNull(answer);

        // 第二轮对话
        answer = myContextApp.doChat(userId,"最近有什么比赛值得看吗？");
        Assertions.assertNotNull(answer);
    }
}
