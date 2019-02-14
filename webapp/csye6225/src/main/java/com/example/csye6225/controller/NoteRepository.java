package com.example.csye6225.controller;

import com.example.csye6225.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Integer> {
}
