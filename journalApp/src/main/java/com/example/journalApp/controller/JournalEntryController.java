package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserModel;
import com.example.journalApp.service.JournalEntryService;
import com.example.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // ✅ 1️⃣ Get all journal entries for a user
    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        Optional<UserModel> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UserModel user = optionalUser.get();
        List<JournalEntry> entries = user.getJournalEntries();

        if (entries != null && !entries.isEmpty()) {
            return ResponseEntity.ok(entries);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No journal entries found for this user");
    }

    // ✅ 2️⃣ Create a new journal entry
    @PostMapping("/{username}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username) {
        try {
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create entry: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ 3️⃣ Get a single journal entry by ID
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        return journalEntry
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ✅ 4️⃣ Delete a journal entry
    @DeleteMapping("/id/{username}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String username, @PathVariable String myId) {
        try {
            journalEntryService.deleteById(myId, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found or could not be deleted");
        }
    }

    // ✅ 5️⃣ Update a journal entry
    @PutMapping("/id/{username}/{myId}")
    public ResponseEntity<?> updateJournalEntry(
            @PathVariable String username,
            @PathVariable String myId,
            @RequestBody JournalEntry newEntry) {

        try {
            JournalEntry old = journalEntryService.findById(myId).orElse(null);
            if (old == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal entry not found");
            }

            if (newEntry.getTitle() != null && !newEntry.getTitle().trim().isEmpty()) {
                old.setTitle(newEntry.getTitle());
            }

            if (newEntry.getContent() != null && !newEntry.getContent().trim().isEmpty()) {
                old.setContent(newEntry.getContent());
            }

            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update entry: " + e.getMessage());
        }
    }
}
