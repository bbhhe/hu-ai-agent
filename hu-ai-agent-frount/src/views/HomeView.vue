<template>
  <div class="home">
    <h1>AI 应用中心</h1>
    <div class="app-grid">
      <router-link to="/love-app" class="app-card">
        <h2>AI 恋爱大师</h2>
        <p>与 AI 恋爱大师进行对话，获取情感建议</p>
      </router-link>
      <router-link to="/manus" class="app-card">
        <h2>AI 超级智能体</h2>
        <p>与 AI 超级智能体进行对话，探索更多可能</p>
      </router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.app-card {
  background: #fff;
  border-radius: 8px;
  padding: 2rem;
  text-decoration: none;
  color: inherit;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

h1 {
  text-align: center;
  color: #2c3e50;
}

h2 {
  color: #42b983;
  margin-bottom: 1rem;
}

p {
  color: #666;
  line-height: 1.6;
}
</style> 