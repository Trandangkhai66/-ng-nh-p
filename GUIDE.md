# Hướng dẫn Capstone Starter Kit

## Yêu cầu
- Docker Desktop, Java 17, Maven, Node 18+

## Cài đặt môi trường (Mac)

| Công cụ | Cách cài | Ghi chú |
|---------|----------|---------|
| **Homebrew** | https://brew.sh | Đã có: `brew -v` |
| **Java 17** | `brew install openjdk@17` | Đã cài. Thêm vào `~/.zshrc`: `export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"` và `export JAVA_HOME="/opt/homebrew/opt/openjdk@17"`. Chạy `source ~/.zshrc` hoặc mở terminal mới. |
| **Maven** | `brew install maven` | Đã cài. `mvn -v` |
| **Docker Desktop** | `brew install --cask docker` (sẽ hỏi mật khẩu sudo) hoặc tải https://www.docker.com/products/docker-desktop | Sau khi cài: mở app **Docker** trong Applications, đợi icon cột máy sáng (Docker đang chạy). Kiểm tra: `docker run hello-world` |
| **Node** | `brew install node` | Thường đã có. `node -v` |

## Biến môi trường / config DB
- **PostgreSQL**: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (mặc định: `jdbc:postgresql://localhost:5432/capstone_db`, admin, admin123).
- **Redis**: `SPRING_REDIS_HOST`, `SPRING_REDIS_PORT` (mặc định localhost:6379).
- **RabbitMQ**: `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASSWORD` (mặc định localhost:5672, admin, admin123).
- Khi chạy `docker-compose up`, backend tự nhận URL từ các service (postgres, redis, rabbitmq). Chạy backend **local** (không qua docker) thì cần Postgres/Redis/RabbitMQ đang chạy và dùng `.env` hoặc export các biến trên (xem `.env.example`).

## 1. Tạo cặp khóa RSA (JWT)
```bash
cd backend
mkdir -p keys
openssl genrsa -out keys/private_key.pem 2048
openssl rsa -in keys/private_key.pem -pubout -out keys/public_key.pem
```

## 2. Google OAuth (Google Cloud Console)
- Tạo project → APIs & Services → OAuth consent screen (External, scope: email, profile)
- Credentials → Create OAuth 2.0 Client ID (Web) → Authorized redirect: `http://localhost:8080/login/oauth2/code/google`
- Tạo file `.env` tại thư mục gốc project:
```
GOOGLE_CLIENT_ID=xxx.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-xxx
```

## 3. Chạy bằng Docker
```bash
docker-compose up --build
```
- Frontend: http://localhost:3000  
- Backend: http://localhost:8080  
- Swagger: http://localhost:8080/swagger-ui.html  
- RabbitMQ UI: http://localhost:15672 (admin/admin123)

## 4. Chạy local (dev)
```bash
# Terminal 1: infra
docker-compose up postgres redis rabbitmq

# Terminal 2: backend (từ thư mục backend, đã có keys + .env)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 3: frontend
cd frontend && npm i && npm run dev
```
- Đặt `GOOGLE_*` trong `.env` hoặc export. Backend đọc từ env.

## 5. Test & API
- Backend test: `cd backend && mvn test`
- Swagger: JWT lấy sau khi Login Google, bấm Authorize, dán `Bearer <token>`.

## Cấu trúc gọn
- `backend/`: Spring Boot (auth, user, worker, scheduler). Module theo folder: controller, service, repository, dto.
- `frontend/`: Vite + React. `features/auth`, `features/user`, `pages/`, `components/`, `services/api.js`.
