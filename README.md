# CryptoCollector

Proyecto sencillo para gestionar criptomonedas con Spring Boot.

Este README explica rápidamente cómo usar Swagger, cómo ejecutar la app con Docker y qué hacer primero (agregar/sincronizar criptomonedas), de forma breve y directa.

---

## Agregar / sincronizar criptomonedas

Antes de usar la API para consultar o filtrar criptomonedas, necesitas que la base de datos tenga datos. debes llenarla asi:

- Usar el endpoint de sincronización que trae datos desde CoinGecko:

  `GET /api/crypto/sync`

  Al llamar a este endpoint la aplicación descargará y guardará (o actualizará) las criptomonedas más relevantes.


---

## Endpoints principales y para qué sirven

- `GET /api/crypto/sync` — Sincroniza los datos desde la API externa (CoinGecko). Úsalo primero para poblar la base de datos.

- `GET /api/crypto/list` — Lista criptomonedas paginadas. Parámetros: `page`, `size`, `sortBy`, `direction`.

- `GET /api/crypto/search?query=...` — Busca por nombre o símbolo (paginado).

- `GET /api/crypto/{id}` — Devuelve los detalles de una criptomoneda por su `id` en la base de datos.

- `GET /api/crypto/filter` — Filtra por rango de precio y market cap. Parámetros opcionales: `minPrice`, `maxPrice`, `minMarketCap`, `maxMarketCap`, además de paginado y orden.

- `POST /api/auth/register` — Registrar un usuario (para obtener acceso a endpoints protegidos).

- `POST /api/auth/login` — Iniciar sesión y obtener un token JWT.

---

## Swagger (documentación de la API)

- URL de Swagger UI (cuando la app esté corriendo):

  http://localhost:8080/swagger-ui.html

- Para probar endpoints protegidos con JWT desde Swagger:
  1. Registra un usuario con `POST /api/auth/register`.
  2. Inicia sesión con `POST /api/auth/login` y copia el token JWT de la respuesta.
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
git clone https://github.com/MaguirreC/cryptoCollecter.git
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

Si quieres, puedo añadir ejemplos de peticiones (curl) para cada endpoint o una guía rápida para pruebas con Postman/Swagger.

Autor: MaguirreC
