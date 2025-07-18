import Login from "./Login";
import SignUp from "./SignUp";
import ForgotPassword from "./ForgotPassword";
import { useState } from 'react';
import './App.css';

function App() {
  const [page, setPage] = useState('login');

  if (page === 'signup') {
    return <SignUp onSwitchToLogin={() => setPage('login')} />;
  }
  if (page === 'forgot') {
    return <ForgotPassword onSwitchToLogin={() => setPage('login')} />;
  }
  return <Login onSwitchToSignUp={() => setPage('signup')} onSwitchToForgot={() => setPage('forgot')} />;
}

export default App;
