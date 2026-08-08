# taskflow-chat

Aplicación de chat en tiempo real con arquitectura fullstack. Los usuarios pueden registrarse, iniciar sesión y chatear en una sala compartida donde los mensajes se actualizan instantáneamente sin necesidad de recargar la página.

🔗 **Demo:** [taskflow-chat-five.vercel.app](https://taskflow-chat-five.vercel.app/login)

> ⚠️ El backend está actualmente pausado por costos de hosting. El frontend carga correctamente y el código fuente completo está disponible en este repositorio.

---

## ¿Cómo funciona?

Cuando un usuario inicia sesión, el frontend abre una conexión WebSocket con el backend enviando el token JWT como parámetro en la URL. El backend valida el token, envía el historial de los últimos 50 mensajes y luego transmite cada nuevo mensaje a todos los clientes conectados en tiempo real.

```
Usuario escribe → frontend envía JSON por WebSocket
→ backend recibe → guarda en PostgreSQL
→ backend transmite a todos los clientes conectados
→ cada frontend recibe → React actualiza la UI
```

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Frontend | React + Vite |
| Backend | Ktor (Kotlin) |
| Base de datos | PostgreSQL |
| Autenticación | JWT |
| Tiempo real | WebSockets nativos |
| Deploy frontend | Vercel |
| Deploy backend | Railway |

---

## Funcionalidades

- Registro de usuarios con validaciones
- Autenticación con JWT
- Mensajería en tiempo real mediante WebSockets nativos
- Historial de mensajes al conectarse
- Reconexión automática si se pierde la conexión
- Indicador de conexión online/offline
- Notificaciones de entrada y salida de usuarios
- UI oscura

---

## Estructura del proyecto

```
taskflow-chat/
├── backend/       # Ktor (Kotlin) — API REST + servidor WebSocket
└── frontend/      # React + Vite — interfaz del chat
```

---

## Correr localmente

### Requisitos

- Node.js v22+
- JDK 21
- Docker (para la base de datos)

### 1. Levantar la base de datos

```bash
cd backend
docker-compose up -d
```

### 2. Levantar el backend

```bash
cd backend
DATABASE_URL=jdbc:postgresql://localhost:5433/taskflow_chat \
DATABASE_USER=tu_usuario \
DATABASE_PASSWORD=tu_contraseña \
JWT_SECRET=tu-secreto \
./gradlew run
```

El backend corre en `http://localhost:8082`.

### 3. Levantar el frontend

Creá un archivo `.env` dentro de `frontend/`:

```
VITE_API_URL=http://localhost:8082
VITE_WS_URL=ws://localhost:8082
```

Luego:

```bash
cd frontend
npm install
npm run dev
```

El frontend corre en `http://localhost:5173`.
