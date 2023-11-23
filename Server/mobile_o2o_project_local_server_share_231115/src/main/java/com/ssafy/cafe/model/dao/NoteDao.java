package com.ssafy.cafe.model.dao;

import java.util.List;

import com.ssafy.cafe.model.dto.Note;

public interface NoteDao {
	
	int insert(Note note);
	
	int delete(Integer id);
	
	Note select(Integer id);
	
	List<Note> selectAll(String id);
	
	int readNote(Integer id);
	
	String getFCMAddNote(String receiverId);

}
