package com.bbhhe.huaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文记忆Advisor
 */
@Slf4j
public class ConversationContextAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // 存储用户对话上下文 (实际项目应考虑持久化)
    private final Map<String, UserContext> userContexts = new ConcurrentHashMap<>();
    private static final int MAX_HISTORY = 5;

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        Map<String, Object> inputMap = advisedRequest.adviseContext();
        String userId = (String) inputMap.get("userId");
        String userMessage = (String) inputMap.get("message");

        // 获取或创建用户上下文
        UserContext userContext = userContexts.computeIfAbsent(userId, k -> new UserContext());

        // 1. 添加上下文到用户消息
        String enhancedPrompt = enhancePromptWithContext(userMessage, userContext);
        //更新用户请求
        Map<String,Object> updateContext = new HashMap<>(inputMap);
        updateContext.put("message", enhancedPrompt);
        AdvisedRequest updateRequest = AdvisedRequest.from(advisedRequest)
                .adviseContext(updateContext)
                .build();

        //2.继续处理
        AdvisedResponse advisedResponse = chain.nextAroundCall(updateRequest);

        //3.更新上下文
        String response =advisedResponse.response().getResult().getOutput().getText();
        updateUserContext(userId,userMessage,response);

        //4.个性化的响应
        String personalizedResponse = personalizeResponse(response, userContext);

        //返回
        return AdvisedResponse.from(advisedResponse)
                .response(ChatResponse.builder()
                        .from(advisedResponse.response())
                        .generations(List.of(new Generation(new AssistantMessage(personalizedResponse))))
                        .build())
                .build();
    }

    private String personalizeResponse(String response, UserContext userContext) {
        if (userContext.getPreferences().contains("体育")) {
            return response + "\n\n(根据您的兴趣，为您推荐最新体育资讯...)";
        }
        return response;
    }

    private void updateUserContext(String userId, String userMessage, String aiResponse) {
        UserContext userContext = userContexts.computeIfAbsent(userId, k -> new UserContext());

        userContext.getConversationHistory().add(new ConversationTurn(userMessage, aiResponse));
        // 保持历史记录不超过最大值
        if (userContext.getConversationHistory().size() > MAX_HISTORY) {
            userContext.getConversationHistory().remove(0);
        }

        // 分析用户偏好
        analyzeUserPreferences(userContext, userMessage);
    }

    private void analyzeUserPreferences(UserContext context, String message) {
        // 简单关键词分析
        if (message.contains("足球") || message.contains("篮球")) {
            context.getPreferences().add("体育");
        }
        if (message.contains("电影") || message.contains("电视剧")) {
            context.getPreferences().add("影视");
        }
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        //TODO 待完成
        return null;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String enhancePromptWithContext(String userMessage, UserContext context) {
        if(context.getConversationHistory().isEmpty()){
            return userMessage;
        }

        StringBuffer prompt = new StringBuffer();
        prompt.append("以下是之前的对话历史:\\n");
        for (ConversationTurn turn : context.getConversationHistory()) {
            prompt.append("用户: ").append(turn.userMessage()).append("\n");
            prompt.append("AI: ").append(turn.aiResponse()).append("\n");
        }
        prompt.append("基于以上上下文，请回答用户的新问题:\n");
        prompt.append(userMessage);
        return prompt.toString();
    }

    //内部上下文类
    private static class UserContext {
        private final List<ConversationTurn> conversationHistory = new LinkedList<>();
        private final Set<String> preferences = new HashSet<>();

        public List<ConversationTurn> getConversationHistory() {
            return conversationHistory;
        }

        public Set<String> getPreferences() {
            return preferences;
        }
    }

    // 对话记录类
    public record ConversationTurn(String userMessage, String aiResponse) {}
}
