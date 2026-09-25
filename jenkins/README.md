# Workshop Jenkins

One command gives you a ready Jenkins that can build and run Docker images:

```bash
cd jenkins
docker compose up -d --build
```

Open http://localhost:8080. There is no setup wizard and no password (fine for a lab PC, **never** for a real server).

Jenkins uses Docker Desktop's engine through `/var/run/docker.sock`, so the images and containers it creates appear in your normal `docker ps`.

Every project has a `Jenkinsfile`. See Step 4 in each project's README.

Stop it with `docker compose down` (add `-v` to delete all Jenkins jobs and history).
