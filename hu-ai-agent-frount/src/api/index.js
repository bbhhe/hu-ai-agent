import axios from "axios";
import { API_CONFIG } from "./config";

// 创建 axios 实例
const api = axios.create(API_CONFIG);

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 在这里可以添加认证信息等
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    // 统一错误处理
    const message =
      error.response?.data?.message || error.message || "请求失败";
    console.error("API Error:", message);
    return Promise.reject(error);
  }
);

// 通用请求方法
export const request = {
  get: (url, params) => api.get(url, { params }),
  post: (url, data) => api.post(url, data),
  put: (url, data) => api.put(url, data),
  delete: (url) => api.delete(url),
};

// SSE 请求方法
export const createSSEConnection = (url, params) => {
  const queryString = new URLSearchParams(params).toString();
  const fullUrl = `${API_CONFIG.baseURL}${url}?${queryString}`;
  return new EventSource(fullUrl);
};

export default api;
