package edu.csce548.project1.service.controller;

import edu.csce548.project1.core.business.CategoryBusinessService;
import edu.csce548.project1.core.model.Category;
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
@RequestMapping("/api/categories")
public class CategoryServiceController {

    private final CategoryBusinessService categoryBusiness = new CategoryBusinessService();

    @GetMapping
    public List<Category> getCategories() throws Exception {
        return categoryBusiness.fetchAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") int id) throws Exception {
        Category category = categoryBusiness.fetchCategory(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody(required = false) CategoryPayload payload) throws Exception {
        if (payload == null || payload.name() == null || payload.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Category created = categoryBusiness.registerCategory(new Category(null, payload.name().trim()));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable("id") int id,
            @RequestBody(required = false) CategoryPayload payload) throws Exception {
        if (payload == null || payload.name() == null || payload.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Category existing = categoryBusiness.fetchCategory(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.name = payload.name().trim();
        boolean updated = categoryBusiness.changeCategory(existing);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") int id) throws Exception {
        boolean deleted = categoryBusiness.removeCategory(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    public record CategoryPayload(String name) {
    }
}
