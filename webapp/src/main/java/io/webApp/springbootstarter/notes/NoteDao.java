package io.webApp.springbootstarter.notes;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.webApp.springbootstarter.notes.Note;
import io.webApp.springbootstarter.notes.NoteRepository;

@Service
public class NoteDao {

	@Autowired
	NoteRepository noterepo;


	public Note Save(Note nt) {
		return noterepo.save(nt);
	}

	
	public List<Note> findAll() {
		return noterepo.findAll();
	}


	public Optional<Note> noteById(String noteid) {
		return noterepo.findById(noteid);
	}

	
	public void deleteNote(String noteid) {
		noterepo.deleteById(noteid);
	}

	public List<Note> findByemailID(String emailID) {
		return noterepo.findByemailID(emailID);
	}

	public Note findNoteUnderEmailList(String NoteID, String emailID) {
		List<Note> NoteList = findByemailID(emailID);
		for (Note i : NoteList) {
			if (i.getId().equals(NoteID))
				return i;
		}
		return null;
	}

	public boolean DeleteNoteUnderEmailList(String NoteID, String emailID) {
		List<Note> NoteList = findByemailID(emailID);
		for (Note i : NoteList) {
			if (i.getId().equals(NoteID)) {
				deleteNote(i.getId());
				return true;
			}
		}
		return false;
	}
}
