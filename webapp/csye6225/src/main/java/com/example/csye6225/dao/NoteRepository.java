package com.example.csye6225.dao;


import com.example.csye6225.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called noteRepository
// CRUD refers Create, Read, Update, Delete

public interface NoteRepository extends JpaRepository<Note, String> {

    List<Note> findByUserID(int userID);
}
