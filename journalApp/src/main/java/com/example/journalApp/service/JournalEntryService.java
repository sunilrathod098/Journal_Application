package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //here we set owner id
        journalEntry.setUserId(user.getId() != null ? user.getId().toHexString() : null);
        JournalEntry saved = journalEntryRepository.save(journalEntry);

        user.getJournalEntries().add(saved);
        userService.saveEntity(user);
    }



    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }



    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }



    public Optional<JournalEntry> findById(String id) {
        try {
            return journalEntryRepository.findById(String.valueOf(new ObjectId(id)));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }



    @Transactional
    public void deleteById(String id, String username) {
        UserModel user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getJournalEntries().removeIf(entity -> entity.getId().toHexString().equals(id));
        userService.saveEntity(user);

        journalEntryRepository.deleteById(String.valueOf(new ObjectId(id)));
    }

    public List<JournalEntry> findByUserId(String userId) {
        return journalEntryRepository.findByUserId(userId);
    }
}
