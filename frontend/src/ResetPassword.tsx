import React, { useState } from "react";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
import "./Login.css"; // style commun

const ResetPassword: React.FC = () => {
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("TOKEN utilisé :", token);

    try {
      await axios.post(`${process.env.REACT_APP_API_URL}/auth/reset-password`, {
        token,
        password,
      });
      console.log("TOKEN utilisé :", token);
      setMessage("✅ Mot de passe réinitialisé avec succès.");
    } catch (error) {
      setMessage("❌ Erreur lors de la réinitialisation.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Réinitialiser le mot de passe 🔑</h2>
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
        {message && (
          <p
            className={
              message.startsWith("✅") ? "success-message" : "error-message"
            }
          >
            {message}
          </p>
        )}
        <p>
          <a href="/login">Retour à la connexion</a>
        </p>
      </div>
    </div>
  );
};

export default ResetPassword;
