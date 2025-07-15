import React, { useEffect, useState } from "react";
import Sidebar from "./Sidebar";
import MainContent from "./MainContent";
import { TaskList, Task } from "./Task";
import {
  getLists,
  getTasks,
  addTask,
  deleteTask,
  updateTask,
  addList,
  deleteList,
  updateList,
} from "./api";
import "./TaskPage.css";

function TaskPage() {
  const [lists, setLists] = useState<TaskList[]>([]);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [selectedListId, setSelectedListId] = useState<number | null>(null);

  // Fetch task lists on mount
  useEffect(() => {
    fetchLists();
  }, []);

  // Fetch tasks when a list is selected
  useEffect(() => {
    if (selectedListId !== null) {
      fetchTasks(selectedListId);
    } else {
      // eslint-disable-next-line @typescript-eslint/no-unused-expressions
      [selectedListId]; // clear tasks if no list is selected
    }
  }, [selectedListId]);

  const fetchLists = async () => {
    try {
      const res = await getLists();
      setLists(res.data);
      if (res.data.length > 0) {
        setSelectedListId(res.data[0].id); // auto-select the first list
      }
    } catch (err) {
      console.error("Erreur lors du chargement des listes :", err);
    }
  };

  const handleDeleteList = async (id: number) => {
    await deleteList(id);
    setSelectedListId(null);
    fetchLists();
  };

  const handleRenameList = async (id: number, newName: string) => {
    await updateList(id, { name: newName });
    fetchLists();
  };

  const fetchTasks = async (listId: number) => {
    const res = await getTasks(listId);
    setTasks(res.data);
  };

  const handleAddList = async (name: string) => {
    if (!name.trim()) return;
    await addList({ name });
    fetchLists();
  };

  const handleAddTask = async (title: string) => {
    if (selectedListId && title.trim()) {
      await addTask({
        title,
        completed: false,
        list: { id: selectedListId, name: "" },
      });
      fetchTasks(selectedListId);
    }
  };

  const handleUpdateTask = async (task: Task) => {
    if (!task.id) return;
    await updateTask(task.id, { ...task, completed: !task.completed });
    fetchTasks(selectedListId!);
  };

  const handleDeleteTask = async (taskId: number) => {
    await deleteTask(taskId);
    fetchTasks(selectedListId!);
  };

  return (
    <div className="task-app">
      <Sidebar
        lists={lists}
        selectedListId={selectedListId}
        onSelectList={setSelectedListId}
        onAddList={handleAddList}
        onDeleteList={handleDeleteList}
        onRenameList={handleRenameList}
      />
      <MainContent
        tasks={tasks}
        onAddTask={handleAddTask}
        onToggleTask={handleUpdateTask}
        onDeleteTask={handleDeleteTask}
      />
    </div>
  );
}

export default TaskPage;
