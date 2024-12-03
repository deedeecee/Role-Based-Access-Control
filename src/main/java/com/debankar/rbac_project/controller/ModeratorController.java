package com.debankar.rbac_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * This controller handles moderator-specific operations. It provides endpoints for moderator-related actions.
 * The controller is designed to be accessed by authenticated users whose roles contain either of MODERATOR or ADMIN.
 */
@RestController
@RequestMapping("/api/v1/moderator")
public class ModeratorController {
    /*
     * The following endpoints can be extended to perform actual business operations.
     * Currently, they serve as placeholders to demonstrate the structure of the controller
     */

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("GET:: moderator controller");
    }

    @PostMapping
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("POST:: moderator controller");
    }

    @PutMapping
    public ResponseEntity<String> put() {
        return ResponseEntity.ok("PUT:: moderator controller");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("DELETE:: moderator controller");
    }
}
