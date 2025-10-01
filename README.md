# Scalable URL Shortener

Production-ready URL shortener service with **Spring Boot**, **PostgreSQL**, **Redis caching**, basic **rate limiting (Bucket4j)**, and **Docker Compose** for local dev. Clean architecture, tests, and a minimal API.

## Features
- Shorten URLs: POST `/api/v1/shorten` → returns `code` and `shortUrl`.
- Redirect: GET `/{code}` → 302 to original URL.
- Base62 codes derived from DB ids (monotonic, compact).
- Redis cache for fast code→URL lookups.
- Basic per-IP rate limiting (60 req/min).
- JPA + PostgreSQL storage, auto-migrations (`ddl-auto=update` for dev).
- Unit tests for core service.

## Tech Stack
Spring Boot 3, Java 17, PostgreSQL, Redis, JPA/Hibernate, Bucket4j, Docker, JUnit.

## Quick Start (Local)
```bash
# start databases
docker compose up -d postgres redis

# run app (requires Java 17 + Maven)
./mvnw spring-boot:run || mvn spring-boot:run
```

App runs at http://localhost:8080

### Shorten a URL
```bash
curl -X POST http://localhost:8080/api/v1/shorten -H 'Content-Type: application/json' -d '{"longUrl":"https://example.com"}'
```

### Follow a Short URL
Open `http://localhost:8080/<code>` in your browser.

## Quick Start (Docker, all-in-one)
```bash
docker compose up --build
```
App: http://localhost:8080

## Configuration
See `src/main/resources/application.yml`. For Docker Compose, env vars override DB/Redis hosts.

## Project Structure
- `controller/` REST endpoints + rate limit filter
- `service/` business logic
- `repository/` JPA repository
- `model/` JPA entity
- `util/` Base62 encoding

## Notes
- `ddl-auto=update` is dev-only. For prod, use Flyway/Liquibase.
- Rate limiting is in-memory per app instance (demo). For distributed rate limits, use Redis-based buckets or API gateway.

## License
MIT
