import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import http from '../api/http'

function Login() {
  const [mode, setMode] = useState('login')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
      e.preventDefault()
      setLoading(true)
      setError('')

      if (mode === 'register') {
        if (username.trim().length < 3) {
          setError('El nombre de usuario debe tener al menos 3 caracteres')
          setLoading(false)
          return
        }
        if (password.length < 6) {
          setError('La contraseña debe tener al menos 6 caracteres')
          setLoading(false)
          return
        }
      }

      try {
        if (mode === 'register') {
          await http.post('/auth/register', { username, email, password })
          setMode('login')
          setError('')
          setPassword('')
          return
        }

        const res = await http.post('/auth/login', { email, password })
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data.user))
        navigate('/chat')
      } catch (err) {
        if (mode === 'login') {
          setError('Email o contraseña incorrectos')
        } else {
          setError('El usuario o email ya existe')
        }
      } finally {
        setLoading(false)
      }
    }

  const switchMode = () => {
    setMode(mode === 'login' ? 'register' : 'login')
    setError('')
    setUsername('')
    setEmail('')
    setPassword('')
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>taskflow chat</h1>
        <p style={styles.subtitle}>
          {mode === 'login' ? 'iniciá sesión para continuar' : 'creá tu cuenta'}
        </p>

        {error && <p style={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit} style={styles.form}>
          {mode === 'register' && (
            <input
              type="text"
              placeholder="nombre de usuario"
              value={username}
              onChange={e => setUsername(e.target.value)}
              style={styles.input}
              required
            />
          )}
          <input
            type="email"
            placeholder="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="password"
            placeholder="contraseña"
            value={password}
            onChange={e => setPassword(e.target.value)}
            style={styles.input}
            required
          />
          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? 'cargando...' : mode === 'login' ? 'entrar' : 'registrarse'}
          </button>
        </form>

        <p style={styles.switchText}>
          {mode === 'login' ? '¿No tenés cuenta?' : '¿Ya tenés cuenta?'}{' '}
          <span onClick={switchMode} style={styles.switchLink}>
            {mode === 'login' ? 'registrate' : 'iniciá sesión'}
          </span>
        </p>
      </div>
    </div>
  )
}

const styles = {
  container: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#0f0f0f',
  },
  card: {
    backgroundColor: '#1a1a1a',
    padding: '40px',
    borderRadius: '12px',
    width: '100%',
    maxWidth: '380px',
    border: '1px solid #2a2a2a',
  },
  title: {
    color: '#ffffff',
    fontSize: '22px',
    fontWeight: '500',
    margin: '0 0 6px 0',
  },
  subtitle: {
    color: '#666',
    fontSize: '14px',
    margin: '0 0 28px 0',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '12px',
  },
  input: {
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
    padding: '12px',
    fontSize: '14px',
    cursor: 'pointer',
    marginTop: '4px',
  },
  error: {
    color: '#f87171',
    fontSize: '13px',
    marginBottom: '12px',
  },
  switchText: {
    color: '#666',
    fontSize: '13px',
    textAlign: 'center',
    marginTop: '20px',
  },
  switchLink: {
    color: '#818cf8',
    cursor: 'pointer',
    textDecoration: 'underline',
  }
}

export default Login