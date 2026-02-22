package edu.csce548.project1.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Hosting platform: Render (Web Service)
 *
 * Deploy steps:
 * 1) Create a Render Web Service from this repository.
 * 2) Build command: mvn -pl taskmanager-service -am clean package
 * 3) Start command: java -jar taskmanager-service/target/taskmanager-service-1.0-SNAPSHOT.jar
 * 4) Set environment variables DB_URL, DB_USER, DB_PASSWORD in Render.
 * 5) Render provides PORT automatically; application.properties maps server.port to PORT.
 *
 * Local run:
 * mvn -f taskmanager-service/pom.xml spring-boot:run
 */
@SpringBootApplication(scanBasePackages = "edu.csce548.project1")
public class TaskManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerServiceApplication.class, args);
    }
}
