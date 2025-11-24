package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserModel;
import com.example.journalApp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminservice;


    //this work as GET /admin/users -> list all users
    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(adminservice.getAllUsers());
    }


    // PUT /admin/block/{userId} -> toggle block/unblock (set active = false)
    @GetMapping("/block/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable String userId) {
        try {
            adminservice.blockUser(userId);
            return ResponseEntity.ok("User blocked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // DELETE /admin/delete/user/{userId}
    @GetMapping("/delete/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            adminservice.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // here we GET /admin/journals -> list all journal entries across users
    @GetMapping("/journals")
    public ResponseEntity<List<JournalEntry>> getAllJournals() {
        return ResponseEntity.ok(adminservice.getAllJournals());
    }


    //here we are delete /admin/delete/journal/{journalId}
    @GetMapping("/delete/journal/{journalId}")
    public ResponseEntity<?> deleteJournal(@PathVariable String journalId) {
        try {
            adminservice.deleteJournal(journalId);
            return ResponseEntity.ok("Journal entry deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
