<template>
  <div class="love-app">
    <header class="app-header">
      <router-link to="/" class="back-button">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" fill="currentColor"/>
        </svg>
        返回
      </router-link>
      <h1>AI 恋爱大师</h1>
    </header>
    <ChatRoom 
      :chat-id="chatId"
      :api-endpoint="apiEndpoint"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'

const chatId = ref('')
const apiEndpoint = 'http://localhost:8123/api/ai/love_app/chat/sse'

onMounted(() => {
  // 生成一个随机的聊天室 ID
  chatId.value = 'love_' + Math.random().toString(36).substr(2, 9)
})
</script>

<style scoped>
.love-app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-light);
}

.app-header {
  padding: var(--spacing-md);
  background: var(--bg-light);
  box-shadow: var(--shadow-sm);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-button {
  position: absolute;
  left: var(--spacing-md);
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  color: var(--primary-color);
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s;
}

.back-button svg {
  width: 24px;
  height: 24px;
}

.back-button:hover {
  color: var(--primary-hover);
}

h1 {
  margin: 0;
  color: var(--text-color);
  font-size: var(--font-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-header {
    padding: var(--spacing-sm);
  }

  .back-button {
    left: var(--spacing-sm);
  }

  .back-button svg {
    width: 20px;
    height: 20px;
  }

  h1 {
    font-size: var(--font-lg);
  }
}

@media (max-width: 480px) {
  .app-header {
    padding: var(--spacing-xs) var(--spacing-sm);
  }

  .back-button {
    font-size: var(--font-sm);
  }

  .back-button svg {
    width: 18px;
    height: 18px;
  }

  h1 {
    font-size: var(--font-md);
  }
}
</style> 