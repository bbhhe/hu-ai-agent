<template>
  <div class="home container">
    <header class="home-header">
      <h1>AI 应用中心</h1>
      <p class="subtitle">探索智能 AI 助手，让生活更轻松</p>
    </header>
    <div class="app-grid">
      <router-link to="/love-app" class="app-card">
        <div class="app-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" fill="currentColor"/>
          </svg>
        </div>
        <h2>AI 恋爱大师</h2>
        <p>与 AI 恋爱大师进行对话，获取专业的情感建议和指导</p>
      </router-link>
      <router-link to="/rag-app" class="app-card">
        <div class="app-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M4 6H2v14c0 1.1.9 2 2 2h14v-2H4V6zm16-4H8c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-1 9h-4v4h-2v-4H9V9h4V5h2v4h4v2z" fill="currentColor"/>
          </svg>
        </div>
        <h2>知识库智能助手</h2>
        <p>基于知识库的智能问答系统，为您提供准确的信息服务</p>
      </router-link>
      <router-link to="/manus" class="app-card">
        <div class="app-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z" fill="currentColor"/>
          </svg>
        </div>
        <h2>AI 超级智能体</h2>
        <p>与 AI 超级智能体进行对话，探索更多可能性和创新</p>
      </router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  min-height: 100vh;
  padding: var(--spacing-xl) var(--spacing-md);
}

.home-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.subtitle {
  color: var(--text-light);
  font-size: var(--font-lg);
  margin-top: var(--spacing-sm);
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-xl);
  max-width: 1200px;
  margin: 0 auto;
}

.app-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: var(--spacing-xl);
  background: var(--bg-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
  text-decoration: none;
  color: var(--text-color);
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.app-icon {
  width: 64px;
  height: 64px;
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
}

.app-icon svg {
  width: 100%;
  height: 100%;
}

.app-card h2 {
  color: var(--primary-color);
  margin-bottom: var(--spacing-md);
  font-size: var(--font-xl);
}

.app-card p {
  color: var(--text-light);
  line-height: 1.6;
  font-size: var(--font-md);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home {
    padding: var(--spacing-lg) var(--spacing-sm);
  }

  .app-grid {
    gap: var(--spacing-lg);
  }

  .app-card {
    padding: var(--spacing-lg);
  }

  .app-icon {
    width: 48px;
    height: 48px;
  }
}

@media (max-width: 480px) {
  .home {
    padding: var(--spacing-md) var(--spacing-xs);
  }

  .app-grid {
    grid-template-columns: 1fr;
  }

  .app-card {
    padding: var(--spacing-md);
  }

  .app-icon {
    width: 40px;
    height: 40px;
  }

  .subtitle {
    font-size: var(--font-md);
  }
}
</style> 