import React, { useState } from "react";
import "./Login.css";

const Login = ({ onSwitchToSignUp, onSwitchToForgot }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Add authentication logic here
    alert(`Logging in as ${email}`);
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Sign in to your account</h2>
        <p className="login-subtitle">Welcome back! Please enter your details.</p>
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