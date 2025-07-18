import React, { useState } from "react";
import "./Login.css";

const ForgotPassword = ({ onSwitchToLogin }) => {
  const [email, setEmail] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    // Simulate sending confirmation email
    setSubmitted(true);
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Forgot Password</h2>
        <p className="login-subtitle">Enter your email to receive a reset link.</p>
        {submitted ? (
          <div style={{ color: '#7da6ff', textAlign: 'center', fontSize: '1rem', marginBottom: '1rem' }}>
            If an account exists for <b>{email}</b>, a reset link has been sent.
          </div>
        ) : (
          <>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <button type="submit">Send Reset Link</button>
          </>
        )}
        <div className="signup-link">
          Remembered your password?{' '}
          <a href="#" onClick={onSwitchToLogin}>Login</a>
        </div>
      </form>
    </div>
  );
};

export default ForgotPassword; 