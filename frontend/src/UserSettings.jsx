import React, { useState, useEffect } from "react"; // 🟢 Импортираш useEffect!
import "./Settings.css";
import axios from "axios";

const UserSettings = ({ onBack }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  useEffect(() => {
    const savedEmail = localStorage.getItem("userEmail");
    if (savedEmail) {
      setEmail(savedEmail);
    }
  }, []);

  const updateUsername = async () => {
    if (!username.trim()) {
      alert("Username cannot be empty.");
      return;
    }

    console.log("Sending UserSettings:", { username, email });

    try {
      const response = await axios.put("http://localhost:8081/api/users/settings/username", {
        username,
        email,
      });

      alert("Username updated!");
      console.log(response.data);
    } catch (error) {
      console.error(error);
      alert("Failed to update username.");
    }
  };

  const updateEmail = async () => {
    if (!email.trim()) {
      alert("Email cannot be empty.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      alert("Please enter a valid email address.");
      return;
    }

    try {
      const response = await axios.put("http://localhost:8081/api/users/settings/email", {
        email,
      });

      alert("Email updated!");
      console.log(response.data);
    } catch (error) {
      console.error(error);
      alert("Failed to update email.");
    }
  };

  const updatePassword = async () => {
    if (!newPassword || !confirmPassword) {
      alert("Please fill in both password fields.");
      return;
    }

    if (newPassword !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    try {
      const response = await axios.put("http://localhost:8081/api/users/settings/password", {
        password: newPassword,
      });

      alert("Password updated!");
      console.log(response.data);
    } catch (error) {
      console.error(error);
      alert("Failed to update password.");
    }
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
        <button type="button" onClick={updateUsername}>
          Update Username
        </button>

        <h3>Email</h3>
        <input
          type="email"
          placeholder="Enter new email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <button type="button" onClick={updateEmail}>
          Update Email
        </button>

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
        <button type="button" onClick={updatePassword}>
          Update Password
        </button>

        <div className="signup-link" style={{ marginTop: "1rem" }}>
          <a
            href="#"
            onClick={(e) => {
              e.preventDefault();
              onBack();
            }}
          >
            ← Back to Home
          </a>
        </div>
      </form>
    </div>
  );
};

export default UserSettings;
