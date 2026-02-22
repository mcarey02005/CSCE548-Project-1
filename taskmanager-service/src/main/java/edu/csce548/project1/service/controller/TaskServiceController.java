package edu.csce548.project1.service.controller;

import edu.csce548.project1.core.business.TaskBusinessService;
import edu.csce548.project1.core.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskServiceController {

    private final TaskBusinessService taskBusiness = new TaskBusinessService();

    @GetMapping
    public List<Task> getTasks() throws Exception {
        return taskBusiness.fetchAllTasks();
    }

    @GetMapping("/with-names")
    public List<String> getTasksWithNames() throws Exception {
        return taskBusiness.fetchAllTasksWithNames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") int id) throws Exception {
        Task task = taskBusiness.fetchTask(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody(required = false) TaskPayload payload) throws Exception {
        if (payload == null || payload.userId() == null || payload.categoryId() == null || payload.description() == null
                || payload.description().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        boolean completed = payload.completed() != null && payload.completed();
        Task created = taskBusiness.registerTask(
                new Task(null, payload.userId(), payload.categoryId(), payload.description().trim(), completed));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") int id, @RequestBody(required = false) TaskPayload payload)
            throws Exception {
        if (payload == null) {
            return ResponseEntity.badRequest().build();
        }

        Task existing = taskBusiness.fetchTask(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (payload.userId() != null) {
            existing.userId = payload.userId();
        }
        if (payload.categoryId() != null) {
            existing.categoryId = payload.categoryId();
        }
        if (payload.description() != null && !payload.description().isBlank()) {
            existing.description = payload.description().trim();
        }
        if (payload.completed() != null) {
            existing.completed = payload.completed();
        }

        boolean updated = taskBusiness.changeTask(existing);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") int id) throws Exception {
        boolean deleted = taskBusiness.removeTask(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    public record TaskPayload(Integer userId, Integer categoryId, String description, Boolean completed) {
    }
}
