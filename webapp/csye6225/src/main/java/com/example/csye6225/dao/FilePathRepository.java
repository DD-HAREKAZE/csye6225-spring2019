
package com.example.csye6225.dao;
import com.example.csye6225.entities.FilePath;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilePathRepository extends CrudRepository<FilePath, String>{
    List<FilePath> findByNoteID(String noteID);
}

