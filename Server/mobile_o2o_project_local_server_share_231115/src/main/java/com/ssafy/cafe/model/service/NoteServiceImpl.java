package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.NoteDao;
import com.ssafy.cafe.model.dto.Note;

@Service
public class NoteServiceImpl implements NoteService {
	
	@Autowired
	NoteDao nDao;
	
	@Override
	public void insert(Note note) {
		nDao.insert(note);
	}

	@Override
	public Note select(Integer id) {
		return nDao.select(id);
	}

	@Override
	public void delete(Integer id) {
		nDao.delete(id);
	}

	@Override
	public List<Note> selectAll() {
		return nDao.selectAll();
	}
  
    
}
