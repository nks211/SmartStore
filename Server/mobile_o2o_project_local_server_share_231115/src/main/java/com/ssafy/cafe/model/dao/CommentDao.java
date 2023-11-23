package com.ssafy.cafe.model.dao;

import java.util.List;
import com.ssafy.cafe.model.dto.Comment;
import com.ssafy.cafe.model.dto.ReComment;

public interface CommentDao {
    int insert(Comment comment);

    int update(Comment comment);

    int delete(Integer commentId);

    Comment select(Integer commentId);

    List<Comment> selectAll();

    List<Comment> selectByProduct(Integer productId);
    
    int insertReComment(ReComment recomment);
    
    List<ReComment> selectReComment(Integer commentId); 
    
    int updateReComment(ReComment recomment);
    
    int deleteReComment(Integer recommentid);
    
    String getFCMAddReComment(ReComment recomment);
    
}
