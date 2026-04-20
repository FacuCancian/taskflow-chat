import { useEffect, useRef, useState, useCallback } from 'react'

export function useWebSocket(token) {
  const [messages, setMessages] = useState([])
  const [connected, setConnected] = useState(false)
  const [error, setError] = useState(null)
  const ws = useRef(null)
  const reconnectTimeout = useRef(null)
  const intentos = useRef(0)
  const maxIntentos = 5

  const connect = useCallback(() => {
    if (!token) return

    ws.current = new WebSocket(`ws://localhost:8082/chat/ws?token=${token}`)

    ws.current.onopen = () => {
      setConnected(true)
      setError(null)
      intentos.current = 0
    }

  ws.current.onmessage = (event) => {
    const msg = JSON.parse(event.data)
    setMessages(prev => {
      if (
        msg.type === 'message' &&
        prev.some(m => m.content === msg.content &&
                       m.username === msg.username &&
                       m._isNew)
      ) {
        return prev
      }
      return [...prev, { ...msg, _isNew: true }]
    })
  }

    ws.current.onclose = () => {
      setConnected(false)
      if (intentos.current < maxIntentos) {
        intentos.current += 1
        const delay = Math.min(1000 * intentos.current, 5000)
        setError(`Conexión perdida. Reconectando (${intentos.current}/${maxIntentos})...`)
        reconnectTimeout.current = setTimeout(connect, delay)
      } else {
        setError('No se pudo reconectar. Recargá la página.')
      }
    }

    ws.current.onerror = () => {
      setError('Error de conexión')
      ws.current?.close()
    }
  }, [token])

  useEffect(() => {
    connect()
    return () => {
      clearTimeout(reconnectTimeout.current)
      ws.current?.close()
    }
  }, [connect])

  const sendMessage = (content) => {
    if (ws.current?.readyState === WebSocket.OPEN) {
      ws.current.send(JSON.stringify({ content }))
      return true
    }
    return false
  }

  return { messages, connected, error, sendMessage }
}