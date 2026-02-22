package edu.csce548.project1.service.controller;

import edu.csce548.project1.core.business.UserBusinessService;
import edu.csce548.project1.core.model.User;
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
@RequestMapping("/api/users")
public class UserServiceController {

    private final UserBusinessService userBusiness = new UserBusinessService();

    @GetMapping
    public List<User> getUsers() throws Exception {
        return userBusiness.fetchAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) throws Exception {
        User user = userBusiness.fetchUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody(required = false) UserPayload payload) throws Exception {
        if (payload == null || payload.name() == null || payload.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        User created = userBusiness.registerUser(new User(null, payload.name().trim()));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody(required = false) UserPayload payload)
            throws Exception {
        if (payload == null || payload.name() == null || payload.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        User existing = userBusiness.fetchUser(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.name = payload.name().trim();
        boolean updated = userBusiness.changeUser(existing);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) throws Exception {
        boolean deleted = userBusiness.removeUser(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    public record UserPayload(String name) {
    }
}
