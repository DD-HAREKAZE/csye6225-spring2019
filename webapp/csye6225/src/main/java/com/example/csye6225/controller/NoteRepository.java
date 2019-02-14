package com.example.csye6225.controller;


import com.example.csye6225.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called noteRepository
// CRUD refers Create, Read, Update, Delete

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUserID(int userID);
}
