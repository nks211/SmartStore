package com.ssafy.cafe.controller.rest;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ssafy.cafe.model.dto.ReComment;
import com.ssafy.cafe.model.service.CommentService;
import com.ssafy.cafe.model.service.FCMService;
import com.ssafy.cafe.model.service.FCMServiceClient;
import com.ssafy.cafe.model.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/comment")
@CrossOrigin("*")
public class CommentRestController {

	
	private static final Logger log = LoggerFactory.getLogger(CommentRestController.class);

	
    @Autowired
    CommentService cService;
    
    @Autowired
    UserService uService;
    
    @Autowired
    private FCMServiceClient fService;
    
    @PostMapping
    @Transactional
    @ApiOperation(value="comment 객체를 추가한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean insert(@RequestBody Comment comment  ) throws IOException {
        cService.addComment(comment);
//        String token = "fv2kNDQxQ6CagVDq3_jeLG:APA91bGtwSFP4XLnb_kzQsYY1Uld8Y2wTk_zD-n2Hxu7XQyNXFgcx_1HeLkcJGjyz-2ePrLfISvCMMJ9lYOZ0zdIVde-cTcHx-KMzylwbd5pu7stAnXk_cY1RjF08XbkeRrbMRejQyUA";
//        fService.sendMessageTo(token, "tests", "bodyyy");
//        fService.sendMessageTo("tests", "bodyyy");
        return true;
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ApiOperation(value="{id}에 해당하는 사용자 정보를 삭제한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean delete(@PathVariable Integer id) {
        cService.removeComment(id);
        return true;
    }
    
    @PutMapping
    @Transactional
    @ApiOperation(value="comment 객체를 수정한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean update(@RequestBody Comment comment  ) {
        cService.updateComment(comment);
        return true;
    }
    
    @PostMapping("/reComment")
    @Transactional
    @ApiOperation(value="body의 comment_id에 해당하는 댓글에 답글을 추가한다", response = Boolean.class)
    public Boolean insertReComment(@RequestBody ReComment recomment) throws IOException {
    	cService.addRecomment(recomment);
    	String fcmToken = cService.getFCMAddReComment(recomment);
    	fService.sendMessageTo("SmartStore", "답글이 달렸습니다.", fcmToken);
    	return true;
    }
    
    @GetMapping("/reComment/{product_id}")
    @Transactional
    @ApiOperation(value = "{product_id}에 해당하는 댓글의 답글들을 불러온다", response = List.class)
    public List<ReComment> getComment(@PathVariable Integer product_id) {
    	return cService.getReComment(product_id);
   	
    }
    
    @PutMapping("/reComment")
    @Transactional
    @ApiOperation(value="reComment 객체를 수정한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean updateReComment(@RequestBody ReComment recomment ) throws IOException {
        cService.updateReComment(recomment);
        String fcmToken = cService.getFCMAddReComment(recomment);
    	fService.sendMessageTo("SmartStore", "답글이 수정되었습니다.", fcmToken);
        return true;
    }
    
    @DeleteMapping("/reComment/{id}")
    @Transactional
    @ApiOperation(value="{id}에 해당하는 답글을 삭제한다. 성공하면 true를 리턴한다. ", response = Boolean.class)
    public Boolean deleteReComment(@PathVariable Integer id) {
        cService.removeReComment(id);
        return true;
    }
    

}
