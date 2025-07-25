import Login from "./Login";
import SignUp from "./SignUp";
import ForgotPassword from "./ForgotPassword";
import UserSettings from "./UserSettings";
import { useState } from 'react';
import './App.css';
import React from "react";

function HomePage() {
  return (
    <div style={{ 
      background: '#10182a', // very dark blue
      minHeight: '100vh', 
      width: '100vw',
      color: '#f3f4f6', 
      textAlign: 'center', 
      margin: '0',
      padding: '0',
      position: 'fixed',
      top: 0,
      left: 0,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 0
    }}>
      <div style={{ paddingTop: '3.5rem', width: '100%' }}>
        <h1>Welcome to Finly</h1>
        <p>Your personal finance assistant.</p>
      </div>
    </div>
  );
}

function Header({ onLoginClick , onHomeClick , onUsernameClick , username}) {
  // Inline style for the link
  const loginLinkStyle = {
    fontSize: "1rem",
    color: "#f3f4f6",
    cursor: "pointer",
    textDecoration: "none",
    outline: "none",
    border: "none",
    background: "none"
  };
  const [isHovered, setIsHovered] = React.useState(false);
  const [isFocused, setIsFocused] = React.useState(false);

  return (
    <header style={{
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      padding: "0.75rem 2rem 0.75rem 2rem",
      background: "#23272f",
      borderBottom: "1px solid #181a20",
      position: "fixed",
      top: 0,
      left: 0,
      width: "100%",
      zIndex: 1000,
      margin: 0,
      boxSizing: "border-box"
    }}>
      <div style={{ fontWeight: "bold", fontSize: "1.5rem", color: "#f3f4f6", letterSpacing: "1px", cursor: 'pointer' }} onClick={onHomeClick} tabIndex={0} aria-label="Home" role="button" onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') onHomeClick(); }}>
        Finly
      </div>
      <div style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#f3f4f6" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="8" r="4"/><path d="M21 21v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2"/></svg>
        <button
          style={{
            ...loginLinkStyle,
            textDecoration: isHovered || isFocused ? "underline" : "none",
            color: "#f3f4f6"
          }}
          onClick={username ? onUsernameClick : onLoginClick}
          onMouseEnter={() => setIsHovered(true)}
          onMouseLeave={() => setIsHovered(false)}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          tabIndex={0}
          aria-label="Log in"
        >
          {username ? username : 'Log in'}
        </button>
      </div>
    </header>
  );
}

function App() {
  const [page, setPage] = useState('home');
  const [usernameFromServer, setUsername] = useState(null);

  let pageContent;
  if (page === 'signup') {
    pageContent = <SignUp onSwitchToLogin={() => setPage('login')} />;
  } else if (page === 'forgot') {
    pageContent = <ForgotPassword onSwitchToLogin={() => setPage('login')} />;
  } else if (page === 'login') {
    pageContent = <Login onSwitchToSignUp={() => setPage('signup')} onSwitchToForgot={() => setPage('forgot')} onLoginSuccess={(usernameFromServer) => {setUsername(usernameFromServer); setPage('home');}} />;
  } else if (page === 'settings') {
     pageContent = (<UserSettings onBack={() => setPage('home')}/>);
   }  else {
    pageContent = <HomePage />;
  }

  return (
    <>
      <Header onLoginClick={() => setPage('login')} onHomeClick={() => setPage('home')}  onUsernameClick={() => setPage('settings')} username = {usernameFromServer}/>
      <div style={{ paddingTop: "3.5rem" }}>
        {pageContent}
      </div>
    </>
  );
}

export default App;
