import React, { useState } from "react";
import "./Login.css";
import axios from "axios";



const SignUp = ({ onSwitchToLogin }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setError("");

    try {
      const response = await axios.post("http://localhost:8081/api/users/signup", {
        username,
        email,
        password
      });

      alert(response.data); // Отговор от Spring Boot

    } catch (err) {
        console.error("Signup error:", err);  // Виждаш целия обект на грешката в конзолата
        if (err.response && err.response.status === 409) {
          setError("User already exists");
        } else if (err.response) {
          setError(`Signup failed: ${err.response.status} ${err.response.data}`);
        } else {
          setError("Signup failed. Please try again.");
        }
      }
  };


  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Create your account</h2>
        <p className="login-subtitle">Sign up to get started!</p>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
        />
        {error && <div style={{ color: '#ff6b6b', textAlign: 'center', fontSize: '0.98rem' }}>{error}</div>}
        <button type="submit">Sign Up</button>
        <div className="signup-link">
          Already have an account?{' '}
          <a href="#" onClick={onSwitchToLogin}>Login</a>
        </div>
      </form>
    </div>
  );
};

export default SignUp; 