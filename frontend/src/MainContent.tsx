import React, { useState } from "react";
import { Task } from "./Task";

interface Props {
  tasks: Task[];
  onAddTask: (title: string) => void;
  onToggleTask: (task: Task) => void;
  onDeleteTask: (id: number) => void;
}

function MainContent({ tasks, onAddTask, onToggleTask, onDeleteTask }: Props) {
  const [newTask, setNewTask] = useState("");

  return (
    <div className="main-content">
      <h2>Mes tâches</h2>

      <ul>
        {tasks.map((task) => (
          <li key={task.id}>
            <span
              onClick={() => onToggleTask(task)}
              className={task.completed ? "completed" : ""}
            >
              {task.title}
            </span>
            <button onClick={() => onDeleteTask(task.id!)}>🗑️</button>
          </li>
        ))}
      </ul>

      <div className="add-task">
        <input
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
          placeholder="Ajoutez une tâche"
        />
        <button
          onClick={() => {
            onAddTask(newTask);
            setNewTask("");
          }}
        >
          Ajouter
        </button>
      </div>
    </div>
  );
}

export default MainContent;
