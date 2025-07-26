import React, { useState, useEffect } from "react"; // 🟢 Импортираш useEffect!
import "./Settings.css";
import axios from "axios";

const UserSettings = ({ onBack }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState({});


  useEffect(() => {
    const savedEmail = localStorage.getItem("userEmail");
    if (savedEmail) {
      setEmail(savedEmail);
    }
  }, []);

  const updateUsername = async () => {
    setErrors({}); // нулирай предишните грешки

    if (!username.trim()) {
      setErrors({ username: "Username cannot be empty." });
      return;
    }

    try {
      const response = await axios.put("http://localhost:8081/api/users/settings/username", {
        username,
        email,
      });

      alert("Username updated!");
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data); // директно взимаш map от backend-а
      } else {
        alert("Failed to update username.");
      }
    }
  };


//   const updateEmail = async () => {
//     if (!email.trim()) {
//       alert("Email cannot be empty.");
//       return;
//     }
//
//     const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//     if (!emailRegex.test(email)) {
//       alert("Please enter a valid email address.");
//       return;
//     }
//
//     try {
//       const response = await axios.put("http://localhost:8081/api/users/settings/email", {
//         email,
//       });
//
//       alert("Email updated!");
//       console.log(response.data);
//     } catch (error) {
//       console.error(error);
//       alert("Failed to update email.");
//     }
//   };

  const updatePassword = async () => {
    setErrors({}); // нулирай грешките

    if (!newPassword || !confirmPassword) {
      setErrors({ password: "Please fill in both password fields." });
      return;
    }

    if (newPassword !== confirmPassword) {
      setErrors({ password: "Passwords do not match." });
      return;
    }

    try {
      const response = await axios.put("http://localhost:8081/api/users/settings/password", {
        password: newPassword,
        email,
      });

      alert("Password updated!");
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data); // получаваме { email: "...", password: "..." }
      } else {
        alert("Failed to update password.");
      }
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
        {errors.username && <p className="error">{errors.username}</p>}
        <button type="button" onClick={updateUsername}>
          Update Username
        </button>

        <h3>Email</h3>
        <input
          type="email"
          placeholder="Enter new email"
          value={email}
          readOnly
          onChange={(e) => setEmail(e.target.value)}
        />
{/*         <button type="button" onClick={updateEmail}> */}
{/*           Update Email */}
{/*         </button> */}

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
        {errors.password && <p className="error">{errors.password}</p>}
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
