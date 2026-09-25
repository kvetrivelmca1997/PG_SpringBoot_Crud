# CRUD Operations: Docker and Jenkins

Spring Boot 2.7 (Java 17) user directory with PostgreSQL, a redesigned UI, Docker support and a Jenkins pipeline.

## Run locally without Docker (as before)

Needs PostgreSQL on localhost:5432 with a `data` schema (original settings):

```bash
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

Open http://localhost:9100

## Run with Docker (no Java, Maven or PostgreSQL needed)

```bash
docker compose up -d --build
docker compose ps               # db (healthy), app (healthy after ~30 s)
```

Open http://localhost:9100. Check health: http://localhost:9100/actuator/health

| File | Purpose |
|---|---|
| `Dockerfile` | Stage 1 builds and tests with Maven + JDK 17. Stage 2 runs `app.jar` on a small JRE as a non-root user, with a healthcheck. |
| `.dockerignore` | Keeps `target/`, IDE files and the Maven wrapper out of the build. |
| `docker-compose.yml` | `db` (PostgreSQL 16, data in the `pgdata` volume) and `app` (waits until the database is healthy). |
| `.env` | Port and database settings. Change a value and run `docker compose up -d`. |

Useful commands:

```bash
docker compose logs -f app
docker compose exec db psql -U postgres -d cruddb -c "SELECT * FROM users;"
docker compose down             # stop, keep data
docker compose down -v          # stop and delete the database
```

## What changed in the code

* `application.properties`: every database setting reads an environment variable, with the old values as defaults.
* `pom.xml`: Actuator (health endpoint), H2 for tests, `finalName` = `app`.
* `src/test/resources/application.properties` + `UserServiceTest`: tests run on in-memory H2, so they work inside `docker build` and Jenkins.
* `UserController`: redirects with `?msg=` for toasts; delete and rename of an unknown ID no longer throw an error; 5 users per page.
* New UI: `static/css/app.css`, `static/index.html` and all three templates.

## Jenkins

1. Start Jenkins with Docker (see the workshop kit's `jenkins/` folder).
2. Push this project to GitHub (Jenkinsfile at the repo root).
3. Jenkins › New Item › Pipeline › Pipeline script from SCM › Git › repo URL (+ `github-pat` credential for private repos) › `*/main` › `Jenkinsfile`.
4. Build Now. Stages: Commit › Build & Test › Deploy › Smoke Test. The CI copy runs on http://localhost:9190.
5. After the first build, every push is built automatically within about 2 minutes (Poll SCM).
