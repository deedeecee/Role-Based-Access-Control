package com.debankar.rbac_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * This controller handles admin-specific operations. It provides endpoints for admin-related actions.
 * The controller is designed to be accessed by authenticated users whose roles contain ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    /*
     * The following endpoints can be extended to perform actual business operations.
     * Currently, they serve as placeholders to demonstrate the structure of the controller
     */

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: admin controller");
    }

    @PostMapping
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: admin controller");
    }

    @PutMapping
    public ResponseEntity<String> put() {
        return ResponseEntity.ok("PUT:: admin controller");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("DELETE:: admin controller");
    }
}
