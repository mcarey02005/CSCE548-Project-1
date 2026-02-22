package edu.csce548.project1.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.csce548.project1.core.model.Category;
import edu.csce548.project1.core.model.Task;
import edu.csce548.project1.core.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * Console frontend that exercises service endpoints.
 *
 * Usage:
 * mvn clean package
 * java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar
 * java -jar taskmanager-console-client/target/taskmanager-console-client-1.0-SNAPSHOT.jar http://localhost:8080/api
 */
public class ServiceConsoleApp {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiBaseUrl;

    public ServiceConsoleApp(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl.endsWith("/") ? apiBaseUrl.substring(0, apiBaseUrl.length() - 1) : apiBaseUrl;
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = args.length > 0 ? args[0] : "http://localhost:8080/api";
        ServiceConsoleApp app = new ServiceConsoleApp(baseUrl);
        app.runMenu();
    }

    private void runMenu() throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Service Console Frontend");
            System.out.println("API base URL: " + apiBaseUrl);
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1) Run full CRUD demo through service layer");
                System.out.println("Q) Quit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();
                if ("1".equals(choice)) {
                    runFullCrudDemo();
                } else if ("q".equalsIgnoreCase(choice)) {
                    return;
                } else {
                    System.out.println("Unknown option");
                }
            }
        }
    }

    private void runFullCrudDemo() throws Exception {
        System.out.println("\n=== USER CRUD DEMO ===");
        User demoUser = demoUserCrud();

        System.out.println("\n=== CATEGORY CRUD DEMO ===");
        Category demoCategory = demoCategoryCrud();

        System.out.println("\n=== TASK CRUD DEMO ===");
        demoTaskCrud();

        if (demoUser != null) {
            sendAndPrint("Final users list", "GET", "/users", null);
        }
        if (demoCategory != null) {
            sendAndPrint("Final categories list", "GET", "/categories", null);
        }
    }

    private User demoUserCrud() throws Exception {
        HttpResponse<String> createResp = sendAndPrint(
                "Create user",
                "POST",
                "/users",
                Map.of("name", "Service Demo User"));
        User created = readIfSuccess(createResp, User.class);
        if (created == null || created.userId == null) {
            return null;
        }

        sendAndPrint("Get created user", "GET", "/users/" + created.userId, null);
        sendAndPrint("List users", "GET", "/users", null);

        sendAndPrint(
                "Update user",
                "PUT",
                "/users/" + created.userId,
                Map.of("name", "Service Demo User Updated"));
        sendAndPrint("Get updated user", "GET", "/users/" + created.userId, null);

        sendAndPrint("Delete user", "DELETE", "/users/" + created.userId, null);
        sendAndPrint("Get deleted user (expect 404)", "GET", "/users/" + created.userId, null);
        return created;
    }

    private Category demoCategoryCrud() throws Exception {
        HttpResponse<String> createResp = sendAndPrint(
                "Create category",
                "POST",
                "/categories",
                Map.of("name", "Service Demo Category"));
        Category created = readIfSuccess(createResp, Category.class);
        if (created == null || created.categoryId == null) {
            return null;
        }

        sendAndPrint("Get created category", "GET", "/categories/" + created.categoryId, null);
        sendAndPrint("List categories", "GET", "/categories", null);

        sendAndPrint(
                "Update category",
                "PUT",
                "/categories/" + created.categoryId,
                Map.of("name", "Service Demo Category Updated"));
        sendAndPrint("Get updated category", "GET", "/categories/" + created.categoryId, null);

        sendAndPrint("Delete category", "DELETE", "/categories/" + created.categoryId, null);
        sendAndPrint("Get deleted category (expect 404)", "GET", "/categories/" + created.categoryId, null);
        return created;
    }

    private void demoTaskCrud() throws Exception {
        HttpResponse<String> userResp = sendAndPrint(
                "Create task owner user",
                "POST",
                "/users",
                Map.of("name", "Task Owner User"));
        User owner = readIfSuccess(userResp, User.class);
        if (owner == null || owner.userId == null) {
            return;
        }

        HttpResponse<String> categoryResp = sendAndPrint(
                "Create task category",
                "POST",
                "/categories",
                Map.of("name", "Task Demo Category"));
        Category category = readIfSuccess(categoryResp, Category.class);
        if (category == null || category.categoryId == null) {
            return;
        }

        Map<String, Object> createPayload = new LinkedHashMap<>();
        createPayload.put("userId", owner.userId);
        createPayload.put("categoryId", category.categoryId);
        createPayload.put("description", "Task created from console client");
        createPayload.put("completed", false);

        HttpResponse<String> createTaskResp = sendAndPrint("Create task", "POST", "/tasks", createPayload);
        Task task = readIfSuccess(createTaskResp, Task.class);
        if (task == null || task.taskId == null) {
            sendAndPrint("Cleanup owner user", "DELETE", "/users/" + owner.userId, null);
            sendAndPrint("Cleanup category", "DELETE", "/categories/" + category.categoryId, null);
            return;
        }

        sendAndPrint("Get created task", "GET", "/tasks/" + task.taskId, null);
        sendAndPrint("List tasks", "GET", "/tasks", null);
        sendAndPrint("List tasks with names", "GET", "/tasks/with-names", null);

        Map<String, Object> updatePayload = new LinkedHashMap<>();
        updatePayload.put("description", "Task updated from console client");
        updatePayload.put("completed", true);

        sendAndPrint("Update task", "PUT", "/tasks/" + task.taskId, updatePayload);
        sendAndPrint("Get updated task", "GET", "/tasks/" + task.taskId, null);

        sendAndPrint("Delete task", "DELETE", "/tasks/" + task.taskId, null);
        sendAndPrint("Get deleted task (expect 404)", "GET", "/tasks/" + task.taskId, null);

        sendAndPrint("Delete task owner user", "DELETE", "/users/" + owner.userId, null);
        sendAndPrint("Delete task category", "DELETE", "/categories/" + category.categoryId, null);
    }

    private HttpResponse<String> sendAndPrint(String label, String method, String path, Object body) throws Exception {
        HttpResponse<String> response = send(method, path, body);
        System.out.println("\n" + label + " -> HTTP " + response.statusCode());
        if (response.body() != null && !response.body().isBlank()) {
            System.out.println(response.body());
        }
        return response;
    }

    private HttpResponse<String> send(String method, String path, Object body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + path))
                .header("Accept", "application/json");

        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            builder.header("Content-Type", "application/json");
            if ("POST".equals(method)) {
                builder.POST(HttpRequest.BodyPublishers.ofString(json));
            } else if ("PUT".equals(method)) {
                builder.PUT(HttpRequest.BodyPublishers.ofString(json));
            } else {
                throw new IllegalArgumentException("Body only supported for POST/PUT in this client");
            }
        } else {
            if ("GET".equals(method)) {
                builder.GET();
            } else if ("DELETE".equals(method)) {
                builder.DELETE();
            } else {
                throw new IllegalArgumentException("Unsupported method: " + method);
            }
        }

        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private <T> T readIfSuccess(HttpResponse<String> response, Class<T> type) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            return null;
        }
        if (response.body() == null || response.body().isBlank()) {
            return null;
        }
        return objectMapper.readValue(response.body(), type);
    }
}
