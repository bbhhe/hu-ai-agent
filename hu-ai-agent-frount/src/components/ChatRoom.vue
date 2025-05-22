<template>
  <div class="chat-room">
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" 
           :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']">
        <div class="message-content">
          {{ message.content }}
        </div>
      </div>
    </div>
    <div class="chat-input">
      <input 
        v-model="inputMessage" 
        @keyup.enter="sendMessage"
        placeholder="输入消息..."
        :disabled="isLoading"
      >
      <button @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
        {{ isLoading ? '发送中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'

const props = defineProps({
  chatId: {
    type: String,
    required: true
  },
  apiEndpoint: {
    type: String,
    required: true
  }
})

const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

watch(messages, scrollToBottom)

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return

  const userMessage = inputMessage.value
  messages.value.push({ type: 'user', content: userMessage })
  inputMessage.value = ''
  isLoading.value = true

  try {
    const eventSource = new EventSource(
      `${props.apiEndpoint}?message=${encodeURIComponent(userMessage)}&chatId=${props.chatId}`
    )

    let aiResponse = ''
    messages.value.push({ type: 'ai', content: '' })

    eventSource.onmessage = (event) => {
      aiResponse += event.data
      messages.value[messages.value.length - 1].content = aiResponse
    }

    eventSource.onerror = () => {
      eventSource.close()
      isLoading.value = false
    }
  } catch (error) {
    console.error('Error:', error)
    messages.value.push({ type: 'ai', content: '抱歉，发生了错误，请稍后重试。' })
    isLoading.value = false
  }
}
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  background: #f5f5f5;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.message {
  margin-bottom: 1rem;
  display: flex;
}

.user-message {
  justify-content: flex-end;
}

.ai-message {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 0.8rem 1rem;
  border-radius: 8px;
  word-break: break-word;
}

.user-message .message-content {
  background: #42b983;
  color: white;
}

.ai-message .message-content {
  background: white;
  color: #2c3e50;
}

.chat-input {
  display: flex;
  gap: 1rem;
}

input {
  flex: 1;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  padding: 0.8rem 1.5rem;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background: #3aa876;
}
</style> 