<template>
  <div class="manus">
    <div class="header">
      <router-link to="/" class="back-button">← 返回</router-link>
      <h1>AI 超级智能体</h1>
    </div>
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
const apiEndpoint = 'http://localhost:8123/api/ai/manus/chat'

onMounted(() => {
  // 生成一个随机的聊天室 ID
  chatId.value = 'manus_' + Math.random().toString(36).substr(2, 9)
})
</script>

<style scoped>
.manus {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  padding: 1rem;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
}

.back-button {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  text-decoration: none;
  color: #42b983;
  font-weight: bold;
}

h1 {
  text-align: center;
  margin: 0;
  color: #2c3e50;
}
</style> 