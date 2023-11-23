package com.ssafy.cafe.model.service;

import java.util.List;
import com.ssafy.cafe.model.dto.Note;

public interface NoteService {
  
    void insert(Note note);
    
    Note select(Integer id);
    
    void delete(Integer id);
    
    List<Note> selectAll(String id);
    
    void readNote(Integer id);
    
    String getFCMAddNote(String receiverId);
    
}
