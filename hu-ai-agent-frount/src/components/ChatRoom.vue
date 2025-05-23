<template>
  <div class="chat-room">
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" 
           :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']">
        <div class="message-avatar" v-if="message.type === 'ai'">
          <img src="../assets/ai-avatar.png" alt="AI Avatar" />
        </div>
        <div class="message-content">
          <div class="message-text">{{ message.content }}</div>
        </div>
        <div class="message-avatar user-avatar" v-if="message.type === 'user'">
          <img src="../assets/user-avatar.png" alt="User Avatar" />
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
      <button class="btn" @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
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
  },
  addNewline: {
    type: Boolean,
    default: false
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
      aiResponse += event.data + (props.addNewline ? '\n' : '')
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
  max-width: 1000px;
  margin: 0 auto;
  padding: var(--spacing-md);
  background: var(--bg-light);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: var(--spacing-md);
  background: var(--bg-color);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-md);
}

.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: var(--spacing-md);
  gap: var(--spacing-sm);
}

.message:last-child {
  margin-bottom: 0;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.message-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  max-width: 70%;
  min-width: 60px;
}

.message-text {
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  word-break: break-word;
  white-space: pre-wrap;
  line-height: 1.5;
}

.user-message {
  flex-direction: row-reverse;
}

.user-message .message-text {
  background: var(--primary-color);
  color: white;
  border-top-right-radius: 0;
}

.ai-message .message-text {
  background: var(--bg-light);
  color: var(--text-color);
  border-top-left-radius: 0;
  text-align: left;
}

.chat-input {
  display: flex;
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  background: var(--bg-light);
  border-top: 1px solid var(--border-color);
}

.chat-input input {
  flex: 1;
  min-width: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-room {
    padding: var(--spacing-sm);
  }

  .message-content {
    max-width: 80%;
  }

  .message-avatar {
    width: 32px;
    height: 32px;
  }
}

@media (max-width: 480px) {
  .chat-room {
    padding: var(--spacing-xs);
  }

  .chat-messages {
    padding: var(--spacing-sm);
  }

  .message-content {
    max-width: 85%;
  }

  .message-avatar {
    width: 28px;
    height: 28px;
  }

  .chat-input {
    padding: var(--spacing-sm);
    gap: var(--spacing-sm);
  }
}
</style> 