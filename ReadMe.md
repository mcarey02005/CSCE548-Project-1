Multi-module Maven layout:
- `taskmanager-core`: data layer (DAO), DB utility, business layer
- `taskmanager-service`: REST service layer that exposes all business methods
- `taskmanager-console-client`: console frontend that calls the service endpoints

Build all modules:
`mvn clean package`

Run service locally:
`mvn -f taskmanager-service/pom.xml spring-boot:run`

Run console frontend:
`java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar`

Run console frontend against another host:
`java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar http://localhost:8080/api`

Database config for service/core:
- Option 1: environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD`
- Option 2: root `config.properties` file (same keys as before)
- Option 3: `-Dtaskmanager.config=<path-to-config.properties>`

Service hosting platform (Render):
- Build command: `mvn -pl taskmanager-service -am clean package`
- Start command: `java -jar taskmanager-service/target/taskmanager-service-1.0-SNAPSHOT.jar`
- Environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`
