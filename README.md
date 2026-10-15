# ScholarPath backend

## Local development

The backend expects a PostgreSQL server. Install/run PostgreSQL locally and create
a database named `scholarpath`. Before starting the application, set these
environment variables in your shell:

| Variable | Local value |
| --- | --- |
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `scholarpath` |
| `DB_USER` | Your local PostgreSQL username |
| `DB_PASSWORD` | Your local PostgreSQL password |

Do not put real credentials in this file or commit them. From this directory,
build and run the application with:

```text
mvn clean package
java -jar target/scholarpath-backend-1.0.0.jar
```

The application listens on port `8080` by default. Spring Boot creates or
updates the tables using Hibernate's `update` setting; it does not migrate
existing MySQL data into PostgreSQL.

## Deploying to Render

Create a **Web Service** from your repository, set its **Root Directory** to
`backend`, and select the **Docker** runtime. Render will use
`backend/Dockerfile` to build the executable JAR and start it; you do not need
to enter separate build or start commands in Render. The Dockerfile runs this
build command and starts the copied JAR as shown:

```text
mvn -B clean package -DskipTests
java -jar /app/app.jar
```

The built artifact is `target/scholarpath-backend-1.0.0.jar`. To run that JAR
directly outside Docker, use
`java -jar target/scholarpath-backend-1.0.0.jar`.

In the Render Web Service's **Environment** settings, add:

| Variable | Value to use |
| --- | --- |
| `DB_HOST` | Hostname from your PostgreSQL database's **Internal Database URL** |
| `DB_PORT` | Port shown in the PostgreSQL dashboard (usually `5432`) |
| `DB_NAME` | Database name shown in the PostgreSQL dashboard |
| `DB_USER` | Username shown in the PostgreSQL dashboard |
| `DB_PASSWORD` | Password shown in the PostgreSQL dashboard; enter it only in Render's secret environment-variable field |
| `APP_CORS_ALLOWED_ORIGINS` | Exact deployed frontend origin(s), such as `https://your-app.vercel.app`; separate multiple origins with commas |
| `SESSION_COOKIE_SAME_SITE` | `none` |
| `SESSION_COOKIE_SECURE` | `true` |

Get the database hostname, port, database name, username, and password from the
existing PostgreSQL instance's **Info** page and connection details in the
Render dashboard. Use the internal hostname when the web service and database
are in the same Render region. Do not paste the full connection URL into
`DB_HOST`.

Render supplies `PORT` to the web service automatically; the application uses
it and falls back to `8080` for local runs. Do not add a `PORT` variable
manually.

The frontend and backend have different origins in production. The `none` and
`true` cookie settings allow the existing session-cookie login flow to work
cross-site over HTTPS. The existing frontend already sends credentials and
the CSRF header. Set the frontend's `VITE_API_BASE_URL` to the deployed backend
origin followed by `/api` (for example, `https://your-backend.onrender.com/api`).

The PostgreSQL database must be reachable from the web service. This change
does not copy or convert any data from the previous MySQL database. If that
data must be retained, migrate it separately before switching the frontend to
the deployed backend.
