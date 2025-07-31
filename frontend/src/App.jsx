import Login from "./Login";
import SignUp from "./SignUp";
import ForgotPassword from "./ForgotPassword";
import UserSettings from "./UserSettings";
import Dashboard from "./Dashboard";
import { useState } from 'react';
import './App.css';
import React from "react";

function HomePage({ onGoToDashboard }) {
  const [isVisible, setIsVisible] = React.useState(false);
  const [currentFeature, setCurrentFeature] = React.useState(0);

  React.useEffect(() => {
    setIsVisible(true);
    const interval = setInterval(() => {
      setCurrentFeature(prev => (prev + 1) % 3);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const features = [
    { icon: "💰", title: "Smart Budgeting", desc: "Track expenses with AI insights" },
    { icon: "📊", title: "Real-time Analytics", desc: "Visualize your spending patterns" },
    { icon: "🎯", title: "Financial Goals", desc: "Set and achieve your targets" }
  ];

  return (
    <div style={{ 
      background: 'linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #4facfe 100%)',
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
      zIndex: 0,
      overflow: 'hidden'
    }}>
      {/* Animated background elements */}
      <div style={{
        position: 'absolute',
        top: '10%',
        left: '10%',
        width: '100px',
        height: '100px',
        background: 'rgba(255, 255, 255, 0.1)',
        borderRadius: '50%',
        animation: 'float 6s ease-in-out infinite'
      }} />
      <div style={{
        position: 'absolute',
        top: '20%',
        right: '15%',
        width: '150px',
        height: '150px',
        background: 'rgba(255, 255, 255, 0.05)',
        borderRadius: '50%',
        animation: 'float 8s ease-in-out infinite reverse'
      }} />
      <div style={{
        position: 'absolute',
        bottom: '30%',
        left: '20%',
        width: '80px',
        height: '80px',
        background: 'rgba(255, 255, 255, 0.08)',
        borderRadius: '50%',
        animation: 'float 7s ease-in-out infinite'
      }} />

      <div style={{ 
        paddingTop: '3.5rem', 
        width: '100%',
        position: 'relative',
        zIndex: 2
      }}>
        {/* Main title with animation */}
        <h1 style={{
          fontSize: '3.5rem',
          fontWeight: '800',
          margin: '0 0 1rem 0',
          background: 'linear-gradient(45deg, #ffffff, #e3f2fd)',
          backgroundClip: 'text',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          opacity: isVisible ? 1 : 0,
          transform: isVisible ? 'translateY(0)' : 'translateY(30px)',
          transition: 'all 0.8s ease-out',
          textShadow: '0 4px 20px rgba(0, 0, 0, 0.3)'
        }}>
          Welcome to Finly
        </h1>

        {/* Subtitle with staggered animation */}
        <p style={{
          fontSize: '1.3rem',
          margin: '0 0 3rem 0',
          opacity: 0.9,
          opacity: isVisible ? 0.9 : 0,
          transform: isVisible ? 'translateY(0)' : 'translateY(20px)',
          transition: 'all 0.8s ease-out 0.2s'
        }}>
          Your personal finance assistant
        </p>

        {/* Feature showcase */}
        <div style={{
          margin: '2rem 0',
          padding: '2rem',
          background: 'rgba(255, 255, 255, 0.1)',
          borderRadius: '20px',
          backdropFilter: 'blur(20px)',
          border: '1px solid rgba(255, 255, 255, 0.2)',
          maxWidth: '500px',
          margin: '2rem auto',
          opacity: isVisible ? 1 : 0,
          transform: isVisible ? 'translateY(0)' : 'translateY(40px)',
          transition: 'all 0.8s ease-out 0.4s'
        }}>
          <div style={{
            fontSize: '4rem',
            margin: '0 0 1rem 0',
            animation: 'bounce 2s ease-in-out infinite'
          }}>
            {features[currentFeature].icon}
          </div>
          <h3 style={{
            fontSize: '1.5rem',
            margin: '0 0 0.5rem 0',
            fontWeight: '600'
          }}>
            {features[currentFeature].title}
          </h3>
          <p style={{
            fontSize: '1rem',
            opacity: 0.8,
            margin: 0
          }}>
            {features[currentFeature].desc}
          </p>
        </div>

        {/* Stats section */}
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          gap: '2rem',
          margin: '2rem 0',
          opacity: isVisible ? 1 : 0,
          transform: isVisible ? 'translateY(0)' : 'translateY(30px)',
          transition: 'all 0.8s ease-out 0.6s'
        }}>
          <div style={{
            textAlign: 'center',
            padding: '1rem',
            background: 'rgba(255, 255, 255, 0.1)',
            borderRadius: '15px',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255, 255, 255, 0.2)',
            minWidth: '120px'
          }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', margin: '0 0 0.5rem 0' }}>10K+</div>
            <div style={{ fontSize: '0.9rem', opacity: 0.8 }}>Active Users</div>
          </div>
          <div style={{
            textAlign: 'center',
            padding: '1rem',
            background: 'rgba(255, 255, 255, 0.1)',
            borderRadius: '15px',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255, 255, 255, 0.2)',
            minWidth: '120px'
          }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', margin: '0 0 0.5rem 0' }}>$2.5M</div>
            <div style={{ fontSize: '0.9rem', opacity: 0.8 }}>Tracked</div>
          </div>
          <div style={{
            textAlign: 'center',
            padding: '1rem',
            background: 'rgba(255, 255, 255, 0.1)',
            borderRadius: '15px',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255, 255, 255, 0.2)',
            minWidth: '120px'
          }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', margin: '0 0 0.5rem 0' }}>99%</div>
            <div style={{ fontSize: '0.9rem', opacity: 0.8 }}>Satisfaction</div>
          </div>
        </div>

        {/* CTA Button */}
        <button 
          style={{
            marginTop: '2rem',
            padding: '1rem 3rem',
            background: 'linear-gradient(135deg, #ffffff, #e3f2fd)',
            color: '#1e3c72',
            border: 'none',
            borderRadius: '50px',
            fontSize: '1.1rem',
            cursor: 'pointer',
            fontWeight: '600',
            boxShadow: '0 10px 30px rgba(0, 0, 0, 0.3)',
            transition: 'all 0.3s ease',
            opacity: isVisible ? 1 : 0,
            transform: isVisible ? 'translateY(0)' : 'translateY(20px)',
            transition: 'all 0.8s ease-out 0.8s'
          }}
          onMouseEnter={(e) => {
            e.target.style.transform = 'translateY(-5px) scale(1.05)';
            e.target.style.boxShadow = '0 15px 40px rgba(0, 0, 0, 0.4)';
          }}
          onMouseLeave={(e) => {
            e.target.style.transform = 'translateY(0) scale(1)';
            e.target.style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.3)';
          }}
          onClick={onGoToDashboard}
        >
          🚀 Get Started
        </button>
      </div>

      <style>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px) rotate(0deg); }
          50% { transform: translateY(-20px) rotate(180deg); }
        }
        @keyframes bounce {
          0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
          40% { transform: translateY(-10px); }
          60% { transform: translateY(-5px); }
        }
      `}</style>
    </div>
  );
}

function Header({ onLoginClick , onHomeClick , onUsernameClick , onDashboardClick, username}) {
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
      <div
        style={{ fontWeight: "bold", fontSize: "1.5rem", color: "#f3f4f6", letterSpacing: "1px", cursor: 'pointer' }}
        onClick={username ? onDashboardClick : onHomeClick}
        tabIndex={0}
        aria-label={username ? "Dashboard" : "Home"}
        role="button"
        onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') (username ? onDashboardClick() : onHomeClick()); }}
      >
        Finly
      </div>
      <div style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#f3f4f6" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <circle cx="12" cy="8" r="4"/>
          <path d="M21 21v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2"/>
        </svg>
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
    pageContent = (
      <Login
        onSwitchToSignUp={() => setPage('signup')}
        onSwitchToForgot={() => setPage('forgot')}
        onLoginSuccess={(data) => {
          setUsername(data.username); // <-- Взимаш само username от обекта
          setPage('dashboard');
        }}
      />
    );
  } else if (page === 'settings') {
    pageContent = <UserSettings onBack={() => setPage('dashboard')} />;
  } else if (page === 'dashboard') {
    pageContent = <Dashboard />;
  } else {
    pageContent = <HomePage onGoToDashboard={() => setPage('dashboard')} />;
  }

  return (
    <>
      <Header
        onLoginClick={() => setPage('login')}
        onHomeClick={() => setPage('home')}
        onDashboardClick={() => setPage('dashboard')}
        onUsernameClick={() => setPage('settings')}
        username={usernameFromServer}
      />
      <div style={{ paddingTop: "3.5rem" }}>
        {pageContent}
      </div>
    </>
  );
}

export default App;
