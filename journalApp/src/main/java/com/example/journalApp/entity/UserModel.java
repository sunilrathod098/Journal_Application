package com.example.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;
    private String password;
    private String email;
    private List<String> roles =  new ArrayList<>();
    private boolean active = true;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();

}
