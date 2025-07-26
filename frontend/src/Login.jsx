import React, { useState } from "react";
import "./Login.css";
import axios from "axios";

const Login = ({ onSwitchToSignUp, onSwitchToForgot, onLoginSuccess  }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);
  const [errors, setErrors] = useState({});

const handleSubmit = async (e) => {
  e.preventDefault();

  const newErrors = {};
  if (!email) newErrors.email = "Email is required";
  if (!password) newErrors.password = "Password is required";

  if (Object.keys(newErrors).length > 0) {
    setErrors(newErrors);
    return;
  }

  try {
    const response = await axios.post("http://localhost:8081/api/users/login", {
      email,
      password
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });

    console.log("Full login response data:", response.data);

    localStorage.setItem("userEmail", response.data.email);
    localStorage.setItem("username", response.data.username);

    alert("Login successful");

    if (onLoginSuccess && response.data) {
      onLoginSuccess(response.data);
    }
  } catch (err) {
    console.error("Login error:", err);
    if (err.response && err.response.data) {
      setErrors({ general: err.response.data.message || "Login failed" });
    } else {
      setErrors({ general: "Server error. Please try again later." });
    }
  }
};




  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Sign in to your account</h2>
        <p className="login-subtitle">Welcome back! Please enter your details.</p>
        {errors.general && <p className="error-message">{errors.general}</p>}
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        {errors.email && <p className="error-message">{errors.email}</p>}
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {errors.password && <p className="error-message">{errors.password}</p>}
        <div className="login-options">
          <label className="remember-me">
            <input
              type="checkbox"
              checked={remember}
              onChange={(e) => setRemember(e.target.checked)}
            />
            Remember me
          </label>
          <a href="#" className="forgot-password" onClick={e => { e.preventDefault(); onSwitchToForgot && onSwitchToForgot(); }}>Forgot password?</a>
        </div>
        <button type="submit">Login</button>
        <div className="signup-link">
          Don&apos;t have an account? <a href="#" onClick={e => { e.preventDefault(); onSwitchToSignUp && onSwitchToSignUp(); }}>Sign up</a>
        </div>
      </form>
    </div>
  );
};

export default Login; 