import { API_ENDPOINTS } from "../config";
import { request, createSSEConnection } from "../index";

// AI 聊天相关的 API 请求
export const aiApi = {
  // 恋爱大师聊天
  loveAppChat: (message, chatId) => {
    return createSSEConnection(API_ENDPOINTS.AI.LOVE_APP_CHAT, {
      message,
      chatId,
    });
  },

  // 超级智能体聊天
  manusChat: (message, chatId) => {
    return request.post(API_ENDPOINTS.AI.MANUS_CHAT, {
      message,
      chatId,
    });
  },
};
