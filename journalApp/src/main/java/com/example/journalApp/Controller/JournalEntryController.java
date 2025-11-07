package com.example.journalApp.Controller;

import com.example.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final Map<Long, JournalEntry> journalEntries = new HashMap<>();


    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry) {
        journalEntries.put(myEntry.getId(), myEntry);
        return true;
    }

    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable Long myId) {
        return journalEntries.get(myId);
    }

    @DeleteMapping("id/{myId}")
    public JournalEntry deleteJournalEntry(@PathVariable Long myId) {
        return journalEntries.remove(myId);
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntry(@PathVariable Long id, @RequestBody JournalEntry myEntry) {
        return journalEntries.put(id, myEntry);
    }
}
