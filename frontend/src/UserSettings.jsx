import React, { useState } from "react";
import "./Settings.css";

const UserSettings = ({ onBack }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const updateUsername = () => {
    if (!username.trim()) {
      alert("Username cannot be empty.");
      return;
    }
    console.log("Updated username:", username);
    alert("Username updated!");
    // TODO: Send to backend
  };

  const updateEmail = () => {
    if (!email.trim()) {
      alert("Email cannot be empty.");
      return;
    }

    // Basic email format check
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      alert("Please enter a valid email address.");
      return;
    }

    console.log("Updated email:", email);
    alert("Email updated!");
    // TODO: Send to backend
  };

  const updatePassword = () => {
    if (!newPassword || !confirmPassword) {
      alert("Please fill in both password fields.");
      return;
    }
    if (newPassword !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }
    console.log("Updated password:", newPassword);
    alert("Password updated!");
    // TODO: Send to backend
  };

  return (
    <div className="settings-container">
      <form className="settings-form" onSubmit={(e) => e.preventDefault()}>
        <h2>User Settings</h2>
        <p className="settings-subtitle">Manage your account preferences</p>

        <h3>Username</h3>
        <input
          type="text"
          placeholder="Enter new username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <button type="button" onClick={updateUsername}>Update Username</button>

        <h3>Email</h3>
        <input
          type="email"
          placeholder="Enter new email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <button type="button" onClick={updateEmail}>Update Email</button>

        <h3>Password</h3>
        <input
          type="password"
          placeholder="New Password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <button type="button" onClick={updatePassword}>Update Password</button>

        <div className="signup-link" style={{ marginTop: "1rem" }}>
          <a href="#" onClick={(e) => { e.preventDefault(); onBack(); }}>
            ← Back to Home
          </a>
        </div>
      </form>
    </div>
  );
};

export default UserSettings;
