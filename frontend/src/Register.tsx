import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Register.css";

function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const validatePassword = (pwd: string) => {
    const rules = [
      { regex: /.{8,}/, label: "Minimum 8 caractères" },
      { regex: /[A-Z]/, label: "Une majuscule" },
      { regex: /[a-z]/, label: "Une minuscule" },
      { regex: /[0-9]/, label: "Un chiffre" },
      { regex: /[^A-Za-z0-9]/, label: "Un caractère spécial" },
    ];
    return rules.filter((rule) => !rule.regex.test(pwd)).map((r) => r.label);
  };

  const validateEmail = (email: string) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  const passwordErrors = validatePassword(password);
  const isPasswordValid = passwordErrors.length === 0;
  const passwordsMatch = password === confirmPassword;
  const isFormValid = username && email && isPasswordValid && passwordsMatch;

  const handleRegister = async () => {
    if (!isFormValid) {
      setError("Veuillez remplir correctement tous les champs.");
      return;
    }

    try {
      await axios.post("http://localhost:8080/auth/register", {
        username,
        email,
        password,
        confirmPassword,
      });

      setMessage("✅ Inscription réussie ! Vérifiez votre email.");
      setError("");
      setTimeout(() => navigate("/login"), 3000);
    } catch (err: any) {
      setError("❌ Erreur lors de l'inscription.");
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2>Inscription</h2>

        {message && <p className="success-message">{message}</p>}
        {error && <p className="error-message">{error}</p>}

        <input
          type="text"
          placeholder="Nom d'utilisateur"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="email"
          placeholder="Adresse email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Mot de passe"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <input
          type="password"
          placeholder="Confirmer le mot de passe"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />

        <div className="password-rules">
          <p>Le mot de passe doit contenir :</p>
          <ul>
            <li className={/.{8,}/.test(password) ? "valid" : "invalid"}>
              ✅ Minimum 8 caractères
            </li>
            <li className={/[A-Z]/.test(password) ? "valid" : "invalid"}>
              ✅ Une majuscule
            </li>
            <li className={/[a-z]/.test(password) ? "valid" : "invalid"}>
              ✅ Une minuscule
            </li>
            <li className={/[0-9]/.test(password) ? "valid" : "invalid"}>
              ✅ Un chiffre
            </li>
            <li className={/[^A-Za-z0-9]/.test(password) ? "valid" : "invalid"}>
              ✅ Un caractère spécial
            </li>
            <li className={passwordsMatch ? "valid" : "invalid"}>
              ✅ Confirmation du mot de passe
            </li>
          </ul>
        </div>

        <button onClick={handleRegister} disabled={!isFormValid}>
          S'inscrire
        </button>

        <p>
          Déjà inscrit ? <a href="/login">Se connecter</a>
        </p>
      </div>
    </div>
  );
}

export default Register;
