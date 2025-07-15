import React, { useState } from "react";
import axios from "axios";
import { useSearchParams } from "react-router-dom";

const ResetPassword: React.FC = () => {
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/auth/reset-password", {
        token,
        password,
      });
      setMessage("Mot de passe réinitialisé avec succès.");
    } catch (error) {
      setMessage("Erreur lors de la réinitialisation.");
    }
  };

  return (
    <div className="auth-container">
      <h2>Réinitialiser le mot de passe</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="password"
          placeholder="Nouveau mot de passe"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Réinitialiser</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default ResetPassword;
