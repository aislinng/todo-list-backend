# Todoit — Backend

API REST para la aplicación de gestión de tareas **Todoit**, construida con Quarkus y autenticación Firebase.

## Descripción

Backend con arquitectura hexagonal que expone endpoints REST para gestionar listas de tareas y usuarios. Utiliza Firebase Authentication para verificar identidades y MySQL como base de datos en producción.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Framework | Quarkus 3.x (JAX-RS / RESTEasy) |
| Lenguaje | Java 17 |
| Base de datos (prod) | MySQL |
| Base de datos (test/dev) | H2 (en memoria) |
| ORM | Hibernate ORM + Panache |
| Autenticación | Firebase Admin SDK |
| Deploy | Render |
| CI | GitHub (auto-deploy en push a `main`) |

## Endpoints principales

```
POST   /api/users              — Registro de usuario (público)
GET    /api/users/me           — Perfil del usuario autenticado
PATCH  /api/users/avatar       — Actualizar avatar

GET    /api/lists              — Listar listas del usuario
POST   /api/lists              — Crear lista
GET    /api/lists/:id          — Obtener lista por ID
PUT    /api/lists/:id          — Actualizar lista
DELETE /api/lists/:id          — Eliminar lista

GET    /api/lists/:id/tasks    — Listar tareas de una lista
POST   /api/lists/:id/tasks    — Crear tarea
PUT    /api/lists/:id/tasks/:taskId   — Actualizar tarea
PATCH  /api/lists/:id/tasks/:taskId  — Marcar como completada/pendiente
DELETE /api/lists/:id/tasks/:taskId  — Eliminar tarea

GET    /api/search?q=          — Buscar listas y tareas
```

Todos los endpoints excepto `POST /api/users` requieren header:
```
Authorization: Bearer <Firebase ID Token>
```

## Variables de entorno

| Variable | Descripción |
|---|---|
| `DB_URL` | URL JDBC de MySQL (`jdbc:mysql://host:3306/db`) |
| `DB_USERNAME` | Usuario de la base de datos |
| `DB_PASSWORD` | Contraseña de la base de datos |
| `FIREBASE_CREDENTIALS` | Ruta al archivo JSON del Service Account de Firebase |
| `PORT` | Puerto HTTP (Render lo inyecta automáticamente) |

## Instalación local

```bash
# Clonar el repositorio
git clone https://github.com/aislinng/todo-list-backend.git
cd todo-list-backend
```

Crea el archivo `src/main/resources/application.properties` con el perfil dev ya configurado para H2. Solo necesitas el archivo de credenciales de Firebase:

```properties
%dev.firebase.credentials=/ruta/a/tu/firebase-admin.json
```

## Cómo ejecutar

```bash
# Modo desarrollo (H2, live reload)
./mvnw quarkus:dev

# Ejecutar tests
./mvnw test

# Empaquetar para producción
./mvnw package -DskipTests
```

El servidor corre en `http://localhost:8080` en modo dev.

## Deploy

El backend está desplegado en **Render**:

🔗 **`https://todo-list-backend-tvzg.onrender.com`**

> El servicio puede tardar ~30 segundos en responder la primera petición si estuvo inactivo (Render free tier).
