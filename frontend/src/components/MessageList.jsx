import { useEffect, useRef } from 'react'

function MessageList({ messages, currentUser }) {
  const bottomRef = useRef(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  return (
    <div style={styles.container}>
      {messages.map((msg, i) => {
        const isOwn = msg.username === currentUser
        const isSystem = msg.type === 'joined' || msg.type === 'left'

        if (isSystem) {
          return (
            <div key={i} style={styles.system}>
              {msg.content}
            </div>
          )
        }

        return (
          <div key={i} style={{
            ...styles.row,
            justifyContent: isOwn ? 'flex-end' : 'flex-start'
          }}>
            <div style={{
              ...styles.bubble,
              backgroundColor: isOwn ? '#4f46e5' : '#1e1e1e',
              borderBottomRightRadius: isOwn ? '4px' : '12px',
              borderBottomLeftRadius: isOwn ? '12px' : '4px',
            }}>
              {!isOwn && (
                <span style={styles.author}>{msg.username}</span>
              )}
              <p style={styles.content}>{msg.content}</p>
              <span style={styles.timestamp}>
                {new Date(msg.timestamp).toLocaleTimeString('es-AR', {
                  hour: '2-digit',
                  minute: '2-digit'
                })}
              </span>
            </div>
          </div>
        )
      })}
      <div ref={bottomRef} />
    </div>
  )
}

const styles = {
  container: {
    flex: 1,
    overflowY: 'auto',
    padding: '20px',
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  row: {
    display: 'flex',
  },
  bubble: {
    maxWidth: '65%',
    padding: '10px 14px',
    borderRadius: '12px',
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
  },
  author: {
    fontSize: '11px',
    color: '#a78bfa',
    fontWeight: '500',
  },
  content: {
    margin: 0,
    fontSize: '14px',
    color: '#fff',
    lineHeight: '1.4',
  },
  timestamp: {
    fontSize: '10px',
    color: '#ffffff55',
    alignSelf: 'flex-end',
  },
  system: {
    textAlign: 'center',
    fontSize: '12px',
    color: '#555',
    padding: '4px 0',
  }
}

export default MessageList