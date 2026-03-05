Multi-module Maven layout:
- `taskmanager-core`: data layer (DAO), DB utility, business layer
- `taskmanager-service`: REST service layer and hosted web frontend (`src/main/resources/static`)
- `taskmanager-console-client`: console frontend that calls the service endpoints

Build all modules:
`mvn clean package`

Run service locally:
`mvn -f taskmanager-service/pom.xml spring-boot:run`

Run console frontend:
`java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar`

Run console frontend against another host:
`java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar http://localhost:8080/api`

Project 3 web frontend:
- URL (local): `http://localhost:8080/`
- API base default: `http://localhost:8080/api`
- Required GET endpoints covered in the UI:
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `GET /api/categories`
  - `GET /api/categories/{id}`
  - `GET /api/tasks`
  - `GET /api/tasks/{id}`
  - `GET /api/tasks/with-names` (subset/read model)

Project 3 submission checklist:
1. Start service and open `http://localhost:8080/`.
2. Click `Run Full GET Demo` to execute all GET calls automatically.
3. Take screenshots showing:
   - main page with successful status
   - users results (all + single)
   - categories results (all + single)
   - tasks results (all + single + with names subset)
4. Save screenshots under `screenshots/Project3/`.

Optional screenshot automation (Windows):
- Command: `.\capture-project3-screenshots.ps1`
- Output:
  - `screenshots/Project3/project3-main-demo.png`
  - `screenshots/Project3/project3-users-categories.png`
  - `screenshots/Project3/project3-tasks-log.png`

Database config for service/core:
- Option 1: environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD`
- Option 2: root `config.properties` file (same keys as before)
- Option 3: `-Dtaskmanager.config=<path-to-config.properties>`

Service hosting platform (Render):
- Build command: `mvn -pl taskmanager-service -am clean package`
- Start command: `java -jar taskmanager-service/target/taskmanager-service-1.0-SNAPSHOT.jar`
- Environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`
