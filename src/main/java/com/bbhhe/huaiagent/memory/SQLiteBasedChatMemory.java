//package com.bbhhe.huaiagent.memory;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.messages.AssistantMessage;
//import org.springframework.ai.chat.messages.MessageType;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import org.springframework.ai.chat.messages.Message;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
///**
// * SQLite-based persistent chat memory implementation
// */
////@Component
//public class SQLiteBasedChatMemory implements ChatMemory {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final String dbPath;
//    private final ObjectMapper objectMapper;
//
//    public SQLiteBasedChatMemory() {
//        this.dbPath = "chat_memory.db";
//        this.objectMapper = new ObjectMapper();
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.sqlite.JDBC");
//        dataSource.setUrl("jdbc:sqlite:" + dbPath);
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    @PostConstruct
//    public void init() {
//        // Create table if it doesn't exist
//        jdbcTemplate.execute("""
//            CREATE TABLE IF NOT EXISTS chat_messages (
//                id TEXT PRIMARY KEY,
//                conversation_id TEXT NOT NULL,
//                message_content TEXT NOT NULL,
//                message_type TEXT NOT NULL,
//                metadata TEXT,
//                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
//            )
//        """);
//    }
//
//    @Override
//    public void add(String conversationId, List<Message> messages) {
//        String sql = "INSERT INTO chat_messages (id, conversation_id, message_content, message_type, metadata) VALUES (?, ?, ?, ?, ?)";
//
//        for (Message message : messages) {
//            try {
//                String metadataJson = message.getMetadata() != null ?
//                        objectMapper.writeValueAsString(message.getMetadata()) : null;
//
//                jdbcTemplate.update(sql,
//                        UUID.randomUUID().toString(),
//                        conversationId,
//                        message.getText(),
//                        message.getMessageType().toString(),
//                        metadataJson
//                );
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to serialize metadata", e);
//            }
//        }
//    }
//
//    @Override
//    public List<Message> get(String conversationId, int lastN) {
//        String sql = """
//            SELECT message_content, message_type, metadata
//            FROM chat_messages
//            WHERE conversation_id = ?
//            ORDER BY created_at DESC
//            LIMIT ?
//        """;
//
//        List<Message> messages = jdbcTemplate.query(sql, new Object[]{conversationId, lastN}, (rs, rowNum) -> {
//            try {
//                Map<String, Object> metadata = null;
//                String metadataJson = rs.getString("metadata");
//                if (metadataJson != null && !metadataJson.isEmpty()) {
//                    metadata = objectMapper.readValue(metadataJson, Map.class);
//                }
//
//                String content = rs.getString("message_content");
//                MessageType messageType = MessageType.valueOf(rs.getString("message_type"));
//
//                // Use appropriate Message implementation based on MessageType
//                switch (messageType) {
//                    case USER:
//                        return new UserMessage(content);
//                    case ASSISTANT:
//                        return new AssistantMessage(content, metadata);
//                    default:
//                        // Fallback for other message types (e.g., SYSTEM)
//                        return new UserMessage(content); // Adjust as needed
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to deserialize metadata", e);
//            }
//        });
//
//        // Reverse the list to get chronological order
//        List<Message> reversedMessages = new ArrayList<>(messages.size());
//        for (int i = messages.size() - 1; i >= 0; i--) {
//            reversedMessages.add(messages.get(i));
//        }
//        return reversedMessages;
//    }
//
//    @Override
//    public void clear(String conversationId) {
//        String sql = "DELETE FROM chat_messages WHERE conversation_id = ?";
//        jdbcTemplate.update(sql, conversationId);
//    }
//}