package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        UserModel user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id) {
        return journalEntryRepository.findById(new ObjectId(id));
    }

    @Transactional
    public void deleteById(String id, String username) {
        UserModel user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getJournalEntries().removeIf(e -> e.getId().toHexString().equals(id));
        userService.saveUser(user);

        journalEntryRepository.deleteById(new ObjectId(id));
    }
}
