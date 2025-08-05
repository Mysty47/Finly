import React, { useState, useEffect } from 'react';
import './Dashboard.css';

function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [currentAccountIndex, setCurrentAccountIndex] = useState(0);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [showAddTransaction, setShowAddTransaction] = useState(false);
  const [showAddAccount, setShowAddAccount] = useState(false);
  const [newTransaction, setNewTransaction] = useState({
    description: '',
    amount: '',
    type: 'expense',
    accountId: null
  });
  const [newAccount, setNewAccount] = useState({
    name: '',
    balance: '0'
  });

  const userEmail = localStorage.getItem('userEmail') || 'demo_user';

  // Fetch accounts from backend
  const fetchAccounts = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8081/api/accounts/${userEmail}`);
      if (!response.ok) {
        throw new Error('Failed to fetch accounts');
      }
      const accountsData = await response.json();
      console.log('Fetched accounts:', accountsData);
      setAccounts(accountsData);
      if (accountsData.length > 0) {
        setNewTransaction(prev => ({ ...prev, accountId: accountsData[0].id }));
      }
    } catch (error) {
      console.error('Error fetching accounts:', error);
      setError('Failed to load accounts');
      // Don't use fallback demo data - just set empty array
      setAccounts([]);
    } finally {
      setLoading(false);
    }
  };

  // Fetch transactions from backend
  const fetchTransactions = async () => {
    try {
      // Get transactions for the current account
      if (accounts.length > 0 && currentAccountIndex < accounts.length) {
        const currentAccount = accounts[currentAccountIndex];
        const response = await fetch(`http://localhost:8081/api/transactions/${userEmail}/${currentAccount.id}`);
        if (!response.ok) {
          throw new Error('Failed to fetch transactions');
        }
        const transactionsData = await response.json();
        console.log('Fetched transactions:', transactionsData);
        setTransactions(transactionsData);
      } else {
        setTransactions([]);
      }
    } catch (error) {
      console.error('Error fetching transactions:', error);
      // Don't use fallback demo data - just set empty array
      setTransactions([]);
    }
  };

  // Create new account
  const createAccount = async () => {
    if (newAccount.name.trim()) {
      try {
        const response = await fetch(`http://localhost:8081/api/accounts/${userEmail}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            name: newAccount.name,
            balance: parseFloat(newAccount.balance),
            username: userEmail
          })
        });

        if (!response.ok) {
          throw new Error('Failed to create account');
        }

        const newAccountData = await response.json();
        console.log('Created account:', newAccountData);
        setAccounts(prev => [...prev, newAccountData]);
        setNewAccount({ name: '', balance: '0' });
        setShowAddAccount(false);
      } catch (error) {
        console.error('Error creating account:', error);
        setError('Failed to create account');
      }
    }
  };

  // Load data on component mount
  useEffect(() => {
    fetchAccounts();
  }, []);

  // Fetch transactions when account changes
  useEffect(() => {
    if (accounts.length > 0) {
      fetchTransactions();
    }
  }, [currentAccountIndex, accounts]);

  const currentAccount = accounts[currentAccountIndex] || { name: 'Loading...', balance: 0 };

  const navigateAccount = (direction) => {
    if (direction === 'prev') {
      setCurrentAccountIndex(prev => 
        prev === 0 ? accounts.length - 1 : prev - 1
      );
    } else {
      setCurrentAccountIndex(prev => 
        prev === accounts.length - 1 ? 0 : prev + 1
      );
    }
  };

  const addTransaction = async () => {
    if (newTransaction.description && newTransaction.amount && newTransaction.accountId) {
      try {
        console.log('Adding transaction with data:', newTransaction);
        
        const transactionData = {
          description: newTransaction.description,
          amount: parseFloat(newTransaction.amount),
          date: new Date().toISOString().split('T')[0],
          type: newTransaction.type
        };

        console.log('Sending transaction data:', transactionData);
        console.log('URL:', `http://localhost:8081/api/transactions/${userEmail}/${newTransaction.accountId}`);

        const response = await fetch(`http://localhost:8081/api/transactions/${userEmail}/${newTransaction.accountId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(transactionData)
        });

        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);

        if (!response.ok) {
          const errorText = await response.text();
          console.error('Response error:', errorText);
          throw new Error(`Failed to add transaction: ${errorText}`);
        }

        const savedTransaction = await response.json();
        console.log('Saved transaction:', savedTransaction);
        
        // Add to local state
        setTransactions(prev => [savedTransaction, ...prev]);
        
        // Refresh accounts to get updated balance
        await fetchAccounts();
        
        setNewTransaction({ 
          description: '', 
          amount: '', 
          type: 'expense',
          accountId: currentAccount.id 
        });
        setShowAddTransaction(false);
      } catch (error) {
        console.error('Error adding transaction:', error);
        setError('Failed to add transaction');
      }
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  if (loading) {
    return (
      <div className="dashboard">
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          fontSize: '1.2rem',
          color: '#f3f4f6'
        }}>
          Loading your financial data...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard">
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          flexDirection: 'column',
          gap: '1rem'
        }}>
          <div style={{ fontSize: '1.2rem', color: '#ef4444' }}>{error}</div>
          <button 
            onClick={() => {
              setError(null);
              fetchAccounts();
              fetchTransactions();
            }}
            style={{
              padding: '0.75rem 1.5rem',
              background: 'linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #4facfe 100%)',
              color: 'white',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer'
            }}
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  // Show message when no accounts exist
  if (accounts.length === 0) {
    return (
      <div className="dashboard">
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          flexDirection: 'column',
          gap: '2rem'
        }}>
          <div style={{ fontSize: '1.5rem', color: '#f3f4f6', textAlign: 'center' }}>
            Welcome to Finly!<br />
            You don't have any accounts yet.
          </div>
          <button 
            onClick={() => setShowAddAccount(true)}
            style={{
              padding: '1rem 2rem',
              background: 'linear-gradient(135deg, #059669 0%, #10b981 100%)',
              color: 'white',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer',
              fontSize: '1.1rem',
              fontWeight: 'bold'
            }}
          >
            Create Your First Account
          </button>
        </div>

        {/* Add Account Modal */}
        {showAddAccount && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>Create Your First Account</h3>
              <div className="form-group">
                <label>Account Name:</label>
                <input
                  type="text"
                  value={newAccount.name}
                  onChange={(e) => setNewAccount(prev => ({ ...prev, name: e.target.value }))}
                  placeholder="Enter account name"
                />
              </div>
              <div className="form-group">
                <label>Initial Balance:</label>
                <input
                  type="number"
                  step="0.01"
                  value={newAccount.balance}
                  onChange={(e) => setNewAccount(prev => ({ ...prev, balance: e.target.value }))}
                  placeholder="Enter initial balance"
                />
              </div>
              <div className="modal-buttons">
                <button onClick={createAccount} className="save-btn">Create Account</button>
                <button onClick={() => setShowAddAccount(false)} className="cancel-btn">Cancel</button>
              </div>
            </div>
          </div>
        )}
      </div>
    );
  }

  return (
    <div className="dashboard">
      {/* Header with Account Selection */}
      <div className="account-header">
        <button 
          className="nav-button" 
          onClick={() => navigateAccount('prev')}
          aria-label="Previous account"
          disabled={accounts.length <= 1}
        >
          &lt;
        </button>
        <h1 className="account-name">{currentAccount.name}</h1>
        <button 
          className="nav-button" 
          onClick={() => navigateAccount('next')}
          aria-label="Next account"
          disabled={accounts.length <= 1}
        >
          &gt;
        </button>
        <button 
          className="add-account-btn"
          onClick={() => setShowAddAccount(true)}
          style={{
            marginLeft: '1rem',
            padding: '0.5rem 1rem',
            background: 'linear-gradient(135deg, #059669 0%, #10b981 100%)',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer',
            fontSize: '0.875rem'
          }}
        >
          + Add Account
        </button>
      </div>

      {/* Balance Section */}
      <div className="balance-section">
        <h2 className="balance-title">BALANCE</h2>
        <div className="balance-amount">
          {formatCurrency(currentAccount.balance)}
        </div>
      </div>

      {/* Transactions Section */}
      <div className="transactions-section">
        <div className="transactions-header">
          <h2 className="transactions-title">TRANSACTIONS</h2>
          <button 
            className="add-transaction-btn"
            onClick={() => setShowAddTransaction(true)}
          >
            +
          </button>
        </div>
        
        <div className="transactions-list">
          {transactions.map(transaction => (
            <div key={transaction.id} className="transaction-item">
              <div className="transaction-info">
                <span className="transaction-description">{transaction.description}</span>
                <span className="transaction-date">{transaction.date}</span>
              </div>
              <span className={`transaction-amount ${transaction.amount >= 0 ? 'positive' : 'negative'}`}>
                {formatCurrency(Math.abs(transaction.amount))}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* Add Account Modal */}
      {showAddAccount && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Add New Account</h3>
            <div className="form-group">
              <label>Account Name:</label>
              <input
                type="text"
                value={newAccount.name}
                onChange={(e) => setNewAccount(prev => ({ ...prev, name: e.target.value }))}
                placeholder="Enter account name"
              />
            </div>
            <div className="form-group">
              <label>Initial Balance:</label>
              <input
                type="number"
                step="0.01"
                value={newAccount.balance}
                onChange={(e) => setNewAccount(prev => ({ ...prev, balance: e.target.value }))}
                placeholder="Enter initial balance"
              />
            </div>
            <div className="modal-buttons">
              <button onClick={createAccount} className="save-btn">Create Account</button>
              <button onClick={() => setShowAddAccount(false)} className="cancel-btn">Cancel</button>
            </div>
          </div>
        </div>
      )}

      {/* Add Transaction Modal */}
      {showAddTransaction && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Add Transaction</h3>
            <div className="form-group">
              <label>Description:</label>
              <input
                type="text"
                value={newTransaction.description}
                onChange={(e) => setNewTransaction(prev => ({ ...prev, description: e.target.value }))}
                placeholder="Enter description"
              />
            </div>
            <div className="form-group">
              <label>Amount:</label>
              <input
                type="number"
                step="0.01"
                value={newTransaction.amount}
                onChange={(e) => setNewTransaction(prev => ({ ...prev, amount: e.target.value }))}
                placeholder="Enter amount"
              />
            </div>
            <div className="form-group">
              <label>Account:</label>
              <select
                value={newTransaction.accountId || ''}
                onChange={(e) => setNewTransaction(prev => ({ ...prev, accountId: parseInt(e.target.value) }))}
              >
                <option value="">Select account</option>
                {accounts.map(account => (
                  <option key={account.id} value={account.id}>
                    {account.name} - {formatCurrency(account.balance)}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Type:</label>
              <select
                value={newTransaction.type}
                onChange={(e) => setNewTransaction(prev => ({ ...prev, type: e.target.value }))}
              >
                <option value="expense">Expense</option>
                <option value="income">Income</option>
              </select>
            </div>
            <div className="modal-buttons">
              <button onClick={addTransaction} className="save-btn">Save</button>
              <button onClick={() => setShowAddTransaction(false)} className="cancel-btn">Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Dashboard; 