import React, { useState } from "react";
import { TaskList } from "./Task";

// interface User {
//   username: string;
//   email: string;
// }

interface Props {
  lists: TaskList[];
  selectedListId: number | null;
  onSelectList: (id: number) => void;
  onAddList: (name: string) => void;
  onDeleteList: (id: number) => void;
  onRenameList: (id: number, newName: string) => void;
}

const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
}

function Sidebar({ lists, selectedListId, onSelectList, onAddList, onDeleteList, onRenameList}: Props) {
  const [newListName, setNewListName] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editValue, setEditValue] = useState("");

  const handleAdd = () => {
    if (newListName.trim()) {
      onAddList(newListName.trim());
      setNewListName("");
    }
  };

  return (
    <div className="sidebar">
      <div className="profile">
        <div className="avatar">AY</div>
        {/* <div className="avatar">
          {user?.username
            ? user.username
                .split(" ")
                .map((word) => word[0])
                .join("")
                .slice(0, 2)
                .toUpperCase()
            : "?"}
        </div> */}
        <button className="logout-btn" onClick={handleLogout}>
          ↩
        </button>
      </div>
      <div className="email">younsiadam2018@gmail.com</div>
      {/* <div className="email">{user?.email || "email inconnu"}</div> */}
      <input placeholder="🔍 Rechercher" className="search" disabled />

      <div className="nav-items">
        <div className="nav-item">🌞 Ma journée</div>
        <div className="nav-item">⭐ Important</div>
        <div className="nav-item">📅 Planifié</div>
        <div className="nav-item">👤 Affectées à moi</div>
        <div className="nav-item">📋 Tâches</div>
      </div>

      <div className="lists">
        {lists.map((list) => (
          <div
            key={list.id}
            className={`list-item ${
              selectedListId === list.id ? "active" : ""
            }`}
          >
            {editingId === list.id ? (
              <input
                value={editValue}
                onChange={(e) => setEditValue(e.target.value)}
                onBlur={() => {
                  if (editValue.trim()) onRenameList(list.id, editValue.trim());
                  setEditingId(null);
                }}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    if (editValue.trim())
                      onRenameList(list.id, editValue.trim());
                    setEditingId(null);
                  }
                }}
                autoFocus
              />
            ) : (
              <>
                <span onClick={() => onSelectList(list.id)}>
                  📄 {list.name}
                </span>
                <div className="list-actions">
                  <button
                    className="rename-btn"
                    onClick={() => {
                      setEditingId(list.id);
                      setEditValue(list.name);
                    }}
                  >
                    ✏️
                  </button>
                  <button
                    className="delete-btn"
                    onClick={() => {
                      if (window.confirm("Supprimer cette liste ?")) {
                        onDeleteList(list.id);
                      }
                    }}
                  >
                    🗑️
                  </button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>

      <div className="add-list">
        <input
          value={newListName}
          onChange={(e) => setNewListName(e.target.value)}
          placeholder="Nouvelle liste"
        />
        <button onClick={handleAdd}>+</button>
      </div>
    </div>
  );
}
    

export default Sidebar;
