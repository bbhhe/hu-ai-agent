<template>
  <div class="rag-app-view">
    <h1>知识库智能助手</h1>
    <p class="description">基于知识库的智能问答系统，为您提供准确的信息服务</p>
    <ChatRoom 
      :chat-id="chatId"
      api-endpoint="rag_app"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { v4 as uuidv4 } from 'uuid'

const chatId = ref('')

onMounted(() => {
  // 生成新的会话ID
  chatId.value = uuidv4()
})
</script>

<style scoped>
.rag-app-view {
  padding: var(--spacing-lg);
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  color: var(--text-color);
  margin-bottom: var(--spacing-sm);
}

.description {
  text-align: center;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-lg);
}

@media (max-width: 768px) {
  .rag-app-view {
    padding: var(--spacing-md);
  }
}
</style> 