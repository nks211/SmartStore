package com.ssafy.cafe.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ssafy.cafe.model.dao.CommentDao;
import com.ssafy.cafe.model.dto.Comment;
import com.ssafy.cafe.model.dto.ReComment;

/**
 * @author taeshik.heo
 * @since 2021. 6. 23.
 */
@Service

public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao cDao;

    @Override
    @Transactional
    public void addComment(Comment comment) {
        cDao.insert(comment);
    }

    @Override
    @Transactional
    public void removeComment(Integer commentId) {
        cDao.delete(commentId);

    }

    @Override
    @Transactional
    public void updateComment(Comment comment) {
        cDao.update(comment);
    }

    @Override
    public List<Comment> selectByProduct(Integer productId) {
        return cDao.selectByProduct(productId);
    }

    @Override
    public Comment selectComment(Integer id) {
        return cDao.select(id);
    }

	@Override
	public void addRecomment(ReComment recomment) {
		cDao.insertReComment(recomment);
		
	}

	@Override
	public ReComment getReComment(Integer comment_id) {
		ReComment rcmt = cDao.selectReComment(comment_id);
		if(rcmt!=null) return rcmt;
		else return new ReComment(comment_id, "");
	}

}
