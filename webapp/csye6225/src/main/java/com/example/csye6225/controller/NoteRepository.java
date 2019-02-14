package com.example.csye6225.controller;

import com.example.csye6225.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findByUserID(int userID);
}
