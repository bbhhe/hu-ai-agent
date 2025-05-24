// API 基础配置
const API_BASE_URL = import.meta.env.PROD
  ? "/api" // 生产环境使用相对路径
  : "http://localhost:8123/api"; // 开发环境指向本地后端服务

// API 请求配置
export const API_CONFIG = {
  baseURL: API_BASE_URL,
  timeout: 30000, // 请求超时时间
  headers: {
    "Content-Type": "application/json",
  },
};

// API 端点配置
export const API_ENDPOINTS = {
  // AI 聊天相关
  AI: {
    LOVE_APP_CHAT: "/ai/love_app/chat/sse",
    MANUS_CHAT: "/ai/manus/chat",
  },
  // 其他模块的 API 端点可以在这里添加
};
