import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Login.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post("http://localhost:8080/auth/login", {
        username,
        password,
      });

      localStorage.setItem("token", response.data.token);

      navigate("/tasks");
    } catch (err) {
      setError("Identifiants invalides.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Connexion 🔐</h2>
        {error && <p className="error-message">{error}</p>}

        <input
          type="text"
          placeholder="Nom d'utilisateur"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <br />

        <input
          type="password"
          placeholder="Mot de passe"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <br />

        <button onClick={handleLogin}>Se connecter</button>
        <p>
          Pas encore de compte ? <a href="/register">S'inscrire</a>
        </p>
        <p>
          <a href="/forgot-password">Mot de passe oublié ?</a>
        </p>
      </div>
    </div>
  );
  
}

export default Login;