package com.debankar.rbac_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * This controller handles user-specific operations. It provides endpoints for user-related actions.
 * The controller is designed to be accessed by authenticated users whose roles contain either of USER, MODERATOR or
 * ADMIN.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    /*
     * The following endpoints can be extended to perform actual business operations.
     * Currently, they serve as placeholders to demonstrate the structure of the controller
     */

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: user controller");
    }

    @PostMapping
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: user controller");
    }

    @PutMapping
    public ResponseEntity<String> put() {
        return ResponseEntity.ok("PUT:: user controller");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("DELETE:: user controller");
    }
}
