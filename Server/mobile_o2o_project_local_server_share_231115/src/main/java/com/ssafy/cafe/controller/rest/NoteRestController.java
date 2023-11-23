package com.ssafy.cafe.controller.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.cafe.model.dto.Comment;
import com.ssafy.cafe.model.dto.Note;
import com.ssafy.cafe.model.dto.ReComment;
import com.ssafy.cafe.model.service.CommentService;
import com.ssafy.cafe.model.service.FCMService;
import com.ssafy.cafe.model.service.NoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/note")
@CrossOrigin("*")
public class NoteRestController {

    @Autowired
    NoteService nService;
    
    @Autowired
    private FCMService fService;
    
    @PostMapping
    @Transactional
    @ApiOperation(value="note 객체를 추가한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean insert(@RequestBody Note note  ) throws IOException {
        nService.insert(note);
        return true;
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ApiOperation(value="{id}에 해당하는 note 정보를 삭제한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean delete(@PathVariable Integer id) {
        nService.delete(id);
        return true;
    }   

}
