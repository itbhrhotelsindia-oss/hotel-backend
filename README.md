# Hotel Backend (Spring Boot + MongoDB)

## Run locally

1. Start MongoDB (docker-compose):
   ```bash
   docker-compose up -d
   ```

2. Build and run:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

3. API base: http://localhost:8080/api/

## Notes
- CORS enabled for http://localhost:5173 (frontend dev server).
- Add frontend build files into `src/main/resources/static` to serve them.
