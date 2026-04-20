import { useState } from 'react'

function MessageInput({ onSend, disabled }) {
  const [text, setText] = useState('')

  const handleSend = () => {
    if (text.trim() === '') return
    onSend(text.trim())
    setText('')
  }

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div style={styles.container}>
      <input
        type="text"
        value={text}
        onChange={e => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder={disabled ? 'conectando...' : 'escribí un mensaje'}
        disabled={disabled}
        style={{
          ...styles.input,
          opacity: disabled ? 0.5 : 1,
        }}
      />
      <button
        onClick={handleSend}
        disabled={disabled || text.trim() === ''}
        style={{
          ...styles.button,
          opacity: disabled || text.trim() === '' ? 0.4 : 1,
        }}
      >
        enviar
      </button>
    </div>
  )
}

const styles = {
  container: {
    display: 'flex',
    gap: '10px',
    padding: '16px 20px',
    borderTop: '1px solid #2a2a2a',
    backgroundColor: '#1a1a1a',
  },
  input: {
    flex: 1,
    backgroundColor: '#111',
    border: '1px solid #2a2a2a',
    borderRadius: '8px',
    padding: '12px 14px',
    color: '#fff',
    fontSize: '14px',
    outline: 'none',
  },
  button: {
    backgroundColor: '#4f46e5',
    color: '#fff',
    border: 'none',
    borderRadius: '8px',
    padding: '12px 20px',
    fontSize: '14px',
    cursor: 'pointer',
    whiteSpace: 'nowrap',
  }
}

export default MessageInput