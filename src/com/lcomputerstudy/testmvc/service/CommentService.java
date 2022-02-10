package com.lcomputerstudy.testmvc.service;

import java.util.ArrayList;

import com.lcomputerstudy.testmvc.dao.CommentDAO;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;


public class CommentService {

	private static CommentService service = null;
	private static CommentDAO dao = null;
	
	private CommentService() {
		
	}
	
	public static CommentService getInstance() {
		if(service == null) {
			service = new CommentService();
			dao = CommentDAO.getInstance();
		}
		return service;
	}
	
	public void insertComment(Comment comment) {
		dao.insertComment(comment);
	}
	
	public Comment detailComment(int c_idx) {
		
		return dao.detailComment(c_idx);
	}
	
	public Comment editComment(int c_idx) {
		return dao.editComment(c_idx);
	}
	
	public void editProcessComment(Comment comment) {
		dao.editProcessComment(comment);
	}
	
	public Comment deleteComment(int c_idx) {
		return dao.deleteComment(c_idx);
	}
	
	public ArrayList<Comment> getComments(Pagination pagination, Board board){
		return dao.getComments(pagination, board);
	}
	
	public int getCommentsCount(Search search,Board board) {
		return dao.getCommentsCount(search,board);
	}

}

