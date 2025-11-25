package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.JournalEntryRepository;
import com.example.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public void blockUser(String userId) {
        Optional<UserModel> optional = userRepository.findById(String.valueOf(new ObjectId(userId)));
        if (optional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        UserModel user = optional.get();
        // Optionally: user could have a field 'active' to toggle
        // Here we repurpose roles or add a property - recommended to add boolean active
        // If your model has no active field, you can remove roles or add that field.
        // For demonstration, assume we set roles = ["BLOCKED"] (not ideal in production)
//        user.setRoles(List.of("BLOCKED"));
        user.setActive(false);
        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(String userId) {
        //here we are remove user's journal first(if it required)
        Optional<UserModel> optional = userRepository.findById(String.valueOf(new ObjectId(userId)));
        if (optional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        UserModel user = optional.get();
        //here we delete referenced journal documents
        if (user.getJournalEntries() != null) {
            user.getJournalEntries().forEach(jrnl -> {
                if (jrnl.getId() != null) {
                    journalEntryRepository.deleteById(String.valueOf(jrnl.getId()));
                }
            });
        }
        userRepository.deleteById(String.valueOf(user.getId()));
    }


    public List<JournalEntry> getAllJournals() {
        return journalEntryRepository.findAll();
    }


    @Transactional
    public void deleteJournal(String journalId) {
        //here we remove reference from owner(s)
        String hex = new ObjectId(journalId).toHexString();
        userRepository.findAll().forEach(user -> {
            if (user.getJournalEntries() != null) {
                user.getJournalEntries().removeIf(jrnl -> jrnl.getId() != null && jrnl.getId().toHexString().equals(hex));
                userRepository.save(user);
            }
        });
        journalEntryRepository.deleteById(String.valueOf((new ObjectId(journalId))));
    }
}
