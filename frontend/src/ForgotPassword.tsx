import React, { useState } from "react";
import axios from "axios";
import "./Login.css"; // utilise le style existant

const ForgotPassword: React.FC = () => {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/auth/forgot-password", { email });
      setMessage("✅ Un email de réinitialisation a été envoyé.");
    } catch (error) {
      setMessage("❌ Erreur lors de l'envoi de l'email.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Mot de passe oublié 🔒</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Entrez votre email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <button type="submit">Envoyer</button>
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

export default ForgotPassword;
