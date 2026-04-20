import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useWebSocket } from '../hooks/useWebSocket'
import MessageList from '../components/MessageList'
import MessageInput from '../components/MessageInput'
import http from '../api/http'

function Chat() {
  const navigate = useNavigate()
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  const { messages, connected, sendMessage } = useWebSocket(token)

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    navigate('/login')
  }
const todosLosMensajes = messages

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <span style={styles.headerTitle}>taskflow chat</span>
        <div style={styles.headerRight}>
          <span style={{
            ...styles.dot,
            backgroundColor: connected ? '#4ade80' : '#f87171'
          }} />
          <span style={styles.username}>{user.username}</span>
          <button onClick={handleLogout} style={styles.logout}>salir</button>
        </div>
      </div>

      <MessageList messages={todosLosMensajes} currentUser={user.username} />
      <MessageInput onSend={sendMessage} disabled={!connected} />
    </div>
  )
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    backgroundColor: '#0f0f0f',
    color: '#fff',
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '14px 20px',
    borderBottom: '1px solid #2a2a2a',
    backgroundColor: '#1a1a1a',
  },
  headerTitle: {
    fontSize: '16px',
    fontWeight: '500',
    color: '#fff',
  },
  headerRight: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
  },
  dot: {
    width: '8px',
    height: '8px',
    borderRadius: '50%',
    display: 'inline-block',
  },
  username: {
    fontSize: '13px',
    color: '#999',
  },
  logout: {
    background: 'none',
    border: '1px solid #2a2a2a',
    borderRadius: '6px',
    color: '#999',
    fontSize: '12px',
    padding: '4px 10px',
    cursor: 'pointer',
  }
}

export default Chat