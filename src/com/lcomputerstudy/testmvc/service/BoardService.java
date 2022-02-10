package com.lcomputerstudy.testmvc.service;

import java.util.ArrayList;

import com.lcomputerstudy.testmvc.dao.BoardDAO;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;

public class BoardService {

		private static BoardService service = null;
		private static BoardDAO dao = null;
		
		private BoardService() {
			
		}
		
		public static BoardService getInstance() {
			if(service == null) {
				service = new BoardService();
				dao = BoardDAO.getInstance();
			}
			return service;
		}
		
//		public ArrayList<Board> getBoards(){
//			return dao.getBoards();
//		}
		
		public void insertBoard(Board board) {
			dao.insertBoard(board);
		}
		
		public Board detailBoard(int b_idx) {
			dao.viewcntBoard(b_idx);
			return dao.detailBoard(b_idx);
		}
		
		public Board editBoard(int b_idx) {
			return dao.editBoard(b_idx);
		}
		
		public void editProcessBoard(Board board) {
			dao.editProcessBoard(board);
		}
		
		public Board deleteBoard(int b_idx) {
			return dao.deleteBoard(b_idx);
		}
		
		public ArrayList<Board> getBoards(Pagination pagination){
			return dao.getBoards(pagination);
		}
		
		public int getBoardsCount(Search search) {
			return dao.getBoardsCount(search);
		}
		
}