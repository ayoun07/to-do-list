import axios from "axios";
import { Task } from "./Task";

const BASE_URL = process.env.REACT_APP_API_URL; // ← depuis .env

// ✅ Instance Axios avec intercepteur
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 🔧 Endpoints des tâches
export const getTasks = (listId: number) =>
  axiosInstance.get(`/api/tasks/list/${listId}`);

export const addTask = (task: Task) => axiosInstance.post(`/api/tasks`, task);

export const deleteTask = (id: number) =>
  axiosInstance.delete(`/api/tasks/${id}`);

export const updateTask = (id: number, task: Task) =>
  axiosInstance.put(`/api/tasks/${id}`, task);

// 🔧 Endpoints des listes
export const getLists = () => axiosInstance.get(`/api/lists`);

export const addList = (list: { name: string }) =>
  axiosInstance.post(`/api/lists`, list);

export const deleteList = (id: number) =>
  axiosInstance.delete(`/api/lists/${id}`);

export const updateList = (id: number, list: { name: string }) =>
  axiosInstance.put(`/api/lists/${id}`, list);
