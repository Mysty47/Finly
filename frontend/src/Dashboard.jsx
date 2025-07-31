import React, { useState, useEffect } from 'react';
import './Dashboard.css';

function Dashboard() {
  const [accounts, setAccounts] = useState([
    { id: 1, name: 'Main Account', balance: 2500.00 },
    { id: 2, name: 'Savings', balance: 5000.00 },
    { id: 3, name: 'Emergency Fund', balance: 3000.00 }
  ]);
  
  const [currentAccountIndex, setCurrentAccountIndex] = useState(0);
  const [transactions, setTransactions] = useState([
    { id: 1, description: 'Grocery shopping', amount: -85.50, date: '2024-01-15', type: 'expense' },
    { id: 2, description: 'Salary deposit', amount: 2500.00, date: '2024-01-10', type: 'income' },
    { id: 3, description: 'Coffee shop', amount: -12.75, date: '2024-01-14', type: 'expense' }
  ]);
  
  const [showAddTransaction, setShowAddTransaction] = useState(false);
  const [newTransaction, setNewTransaction] = useState({
    description: '',
    amount: '',
    type: 'expense'
  });

  const currentAccount = accounts[currentAccountIndex];

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

  const addTransaction = () => {
    if (newTransaction.description && newTransaction.amount) {
      const transaction = {
        id: Date.now(),
        description: newTransaction.description,
        amount: parseFloat(newTransaction.amount) * (newTransaction.type === 'expense' ? -1 : 1),
        date: new Date().toISOString().split('T')[0],
        type: newTransaction.type
      };
      
      setTransactions(prev => [transaction, ...prev]);
      
      // Update account balance
      setAccounts(prev => prev.map((account, index) => 
        index === currentAccountIndex 
          ? { ...account, balance: account.balance + transaction.amount }
          : account
      ));
      
      setNewTransaction({ description: '', amount: '', type: 'expense' });
      setShowAddTransaction(false);
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  return (
    <div className="dashboard">
      {/* Header with Account Selection */}
      <div className="account-header">
        <button 
          className="nav-button" 
          onClick={() => navigateAccount('prev')}
          aria-label="Previous account"
        >
          &lt;
        </button>
        <h1 className="account-name">{currentAccount.name}</h1>
        <button 
          className="nav-button" 
          onClick={() => navigateAccount('next')}
          aria-label="Next account"
        >
          &gt;
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