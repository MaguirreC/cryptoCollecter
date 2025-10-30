# CryptoCollector

Proyecto sencillo para gestionar criptomonedas con Spring Boot.

Este README sólo explica cómo usar Swagger (documentación interactiva) y cómo ejecutar la aplicación con Docker, de forma breve y directa.

---

## Swagger (documentación de la API)

- URL de Swagger UI (cuando la app esté corriendo):

  http://localhost:8080/swagger-ui.html

- Para probar endpoints protegidos con JWT desde Swagger:
  1. Registra un usuario con `POST /auth/register`.
  2. Inicia sesión con `POST /auth/login` y copia el token JWT de la respuesta.
  3. En Swagger UI haz clic en el botón "Authorize" (ícono del candado).
  4. Pega el token con el prefijo `Bearer `, por ejemplo:
     `Bearer eyJhbGci...`.
  5. Cierra el diálogo y ahora podrás ejecutar los endpoints que requieren autenticación.

Consejo rápido: si ves `403` o `401`, revisa que el token no haya expirado y que lo pegaste con `Bearer ` delante.

---

## Docker (levantar la app y la base de datos)

La forma más rápida es usar `docker-compose`, que ya está incluido en el proyecto y configura PostgreSQL + la aplicación.

Pasos mínimos:

1. Clona el repositorio y entra en la carpeta del proyecto:

```cmd
git clone <url-del-repositorio>
cd cryptoCollector
```

2. Levanta los contenedores (con compilación):

```cmd
docker-compose up --build
```

3. Opciones útiles:

```cmd
# Ejecutar en segundo plano
docker-compose up -d

# Ver logs de la aplicación
docker-compose logs -f app

# Detener contenedores
docker-compose down

# Detener y borrar volúmenes (limpieza completa)
docker-compose down -v
```

Qué hace el `docker-compose.yml` del proyecto (resumen):
- Crea un servicio `postgres` con la base de datos `crypto_db` y credenciales definidas.
- Crea un servicio `app` que se construye desde el `Dockerfile` y usa el perfil `docker`.
- Expone el puerto 8080 para acceder a la API y a Swagger UI.

Notas rápidas:
- Si necesitas cambiar credenciales o puertos, edita `docker-compose.yml` o las propiedades en `src/main/resources/application-docker.properties`.
- En Windows asegúrate de tener Docker Desktop en ejecución antes de ejecutar `docker-compose`.

---

Si quieres, puedo añadir una sección con variables de entorno recomendadas o un ejemplo de `docker-compose.override.yml` para desarrollo local.

Autor: MaguirreC

