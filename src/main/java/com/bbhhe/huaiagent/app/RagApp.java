package com.bbhhe.huaiagent.app;

import com.bbhhe.huaiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class RagApp {

    private final ChatClient chatClient;

    public RagApp(ChatModel dashscopeChatModel) {
        //初始化基于内存的对话记忆
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem("你是一个知识库助手，负责根据用户的问题从知识库中获取相关信息，并以Markdown格式输出答案。请确保回答清晰简洁。")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    @Resource
    private VectorStore pgVectorVectorStore;

    public String doChatWithRag(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        //log.info("content:{}",text);
        return text;
    }

    public Flux<ChatResponse> doChatWithRagByStream(String message, String chatId){
        //QuestionA؜nswerAdvi⁢sor 查询增强
//        return chatClient.prompt()
//                .user(message)
//                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                .advisors(new MyLoggerAdvisor())
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
//                .stream()
//                .content();

        //Retrieval؜AugmentationAdvisor 查询增强
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(pgVectorVectorStore)
                        .build())
                .build();

        return chatClient.prompt()
                              .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                       .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(advisor)
                .user(message)
                .stream()
                .chatResponse();
    }
}
