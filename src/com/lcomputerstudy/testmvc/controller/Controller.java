package com.lcomputerstudy.testmvc.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lcomputerstudy.testmvc.service.BoardService;
import com.lcomputerstudy.testmvc.service.CommentService;
import com.lcomputerstudy.testmvc.service.FileService;
import com.lcomputerstudy.testmvc.service.UserService;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.FileUpload;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;
import com.lcomputerstudy.testmvc.vo.User;
//import com.sun.tools.javac.jvm.Items;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;


@WebServlet("*.do")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");

		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		String view = null;
		String idx = null;
		String pw = null;
		HttpSession session = null;
		int page = 1;
		int b_idx = 0;
		int c_idx = 0;
		Board boardVo = null;
		Comment commentVo = null;
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sDate =new SimpleDateFormat(pattern);
		int count = 0;
		Search search = null;
		String strType = null;
		int type = 0;
		String keyword = null;
//		Pagination pagination = null;
		Board board = new Board();
		Comment comment = new Comment();
		String auth = null;
		UserService userSvc = null;
		CommentService commentService = null;
		boolean isRedirected = false;
		FileUpload file = new FileUpload();
		FileService fileService = null;
		File uploadFile = null;
		MultipartRequest mFile = null;
		
		
		command = checkSession(request, response, command);
		
	
		
		ArrayList<Board> boardlist;
		switch (command) {
		
			case "/user-list.do":
				String reqPage = request.getParameter("page");
				if (reqPage != null) { 
					page = Integer.parseInt(reqPage);
				}
				UserService userService = UserService.getInstance();
				ArrayList<User> list = userService.getUsers(page);
				count = userService.getUsersCount();
				
				Pagination pagination = new Pagination(page, count);
				view = "user/list";
				request.setAttribute("list", list);
				request.setAttribute("pagination", pagination);
				break;
			case "/user-insert.do":
				view = "user/insert";
				break;
			case "/user-insert-process.do":
				User user = new User();
				user.setU_id(request.getParameter("id"));
				user.setU_pw(request.getParameter("password"));
				user.setU_name(request.getParameter("name"));
				user.setU_tel(request.getParameter("tel1") + "-" + request.getParameter("tel2") + "-" + request.getParameter("tel3"));
				user.setU_age(request.getParameter("age"));
				
				userService = UserService.getInstance();
				userService.insertUser(user);
						
				view = "user/insert-result";
				break;
				
			case "/user-login.do":
				view = "user/login";
				break;

			case "/user-login-process.do":
				idx = request.getParameter("login_id");
				pw = request.getParameter("login_password");
				
				userService = UserService.getInstance();
				user = userService.loginUser(idx,pw);
							
				if(user != null) {
					HttpSession session1 = request.getSession();
					
					session1.setAttribute("user", user);
					
						
					view = "user/login-result";

					
				} else {
					view = "user/login-fail";
				}			
				break;
			case "/logout.do":
				session = request.getSession();
				session.invalidate();
				view = "user/login";
				break;
				
			case "/access-denied.do":
				
				view = "user/access-denied";
				break;
				
			case "/board-list.do":
				
				String reqPage1 = request.getParameter("page");
				if(reqPage1 != null) {
					page = Integer.parseInt(reqPage1);
				}
				
				strType = request.getParameter("type");
				if (strType != null && !strType.equals("")) {
					type = Integer.parseInt(request.getParameter("type"));
					keyword = request.getParameter("keyword");
					search = new Search(type, keyword);
				}
				
				BoardService boardService = BoardService.getInstance();
				count = boardService.getBoardsCount(search);
				pagination = new Pagination(page,count,search);
				ArrayList<Board> list1 = boardService.getBoards(pagination);
				
				request.setAttribute("list", list1);
				request.setAttribute("pagination", pagination);
				view = "board/list";
				break;
			
			case "/board-insert.do":
				board = new Board(); 
				board.setB_group(Integer.parseInt(request.getParameter("b_group")));
				board.setB_order(Integer.parseInt(request.getParameter("b_order")));
				board.setB_depth(Integer.parseInt(request.getParameter("b_depth")));

				request.setAttribute("board", board);
				view = "board/insert";
				break;
				
			case "/board-insert-process.do":
				
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
				
				 String CHARSET = "utf-8";
				 String ATTACHES_DIR = "C:\\Users\\l2-evening\\Documents\\work10\\lcomputerstudy2-2.10 apache\\WebContent\\img";
				 
				 int LIMIT_SIZE_BYTES = 1024 * 1024;


			    response.setContentType("text/html; charset=UTF-8");
		        request.setCharacterEncoding(CHARSET);
		        PrintWriter out = response.getWriter();


		 
		        File attachesDir = new File(ATTACHES_DIR);
		 
		        	
		        
		        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		        fileItemFactory.setRepository(attachesDir);
		        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
		        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
		    	
		        session = request.getSession();
				user = (User)session.getAttribute("user");
				board = new Board();
				board.setU_idx(user.getU_idx());
				board.setB_date(sDate.format(new Date()));
				
		        try {
		            List<FileItem> items = fileUpload.parseRequest(request);
		            String title ="", content="", writer="",b_group="",b_order="",b_depth="";
		            ArrayList list10 = new ArrayList();
		            for (FileItem item : items) {
		            	
		            	
		                if (item.isFormField()) {
		                	
		                	String controlName = item.getFieldName();
		                    String controlValue = item.getString();
		                	
		                    if("title".equals(controlName)) {
		                		title = controlValue;
		                		board.setB_title(title);
		                	}
		                	if("content".equals(controlName)) {
			                	content = controlValue;
			                	board.setB_content(content);
		                	}
		                	if("writer".equals(controlName)) {
			                	writer = controlValue;
			                	board.setB_writer(writer);
		                	}
		                	if("b_group".equals(controlName)) {
		                		b_group = controlValue;
			                	board.setB_group(Integer.parseInt(b_group));
		                	}
		                	if("b_order".equals(controlName)) {
		                		b_order = controlValue;
			                	board.setB_order(Integer.parseInt(b_order));
		                	}
		                	if("b_depth".equals(controlName)) {
		                		b_depth = controlValue;
			                	board.setB_depth(Integer.parseInt(b_depth));
		                	}
		                	
		                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(),item.getString(CHARSET));
		                    
		    				
		                } else {
		                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
		                            item.getName(), item.getSize());
		                    if (item.getSize() > 0) {
		                        String separator = File.separator;
		                        int index =  item.getName().lastIndexOf(separator);
		                        String fileName = item.getName().substring(index  + 1);
		                        uploadFile = new File(ATTACHES_DIR +  separator + fileName);
		                        if(uploadFile.exists()) {
		                        	for(int k=0; true; k++) {
		                        		uploadFile = new File(ATTACHES_DIR,"("+k+")"+fileName);
		                        		if(!uploadFile.exists()) {
		                        			fileName = "("+k+")"+fileName;
		                        			
		                        		
		                        			break;
		                        			
		                        		}
		                        	}
		                        }
		                        
		                    
		                        
		                        item.write(uploadFile);
		                        list10.add(uploadFile);
		                        //boardFile = new BoardFile();
		                        //boardFileList.add(boardFile)
		                        
		                    }
		               }
		            }
		            boardService = BoardService.getInstance();
		            board=boardService.insertBoard(board);
					  for (int i = 0; i < list10.size(); ++i) {
						  fileService = FileService.getInstance();
						  uploadFile=(File) list10.get(i);
						  fileService.insertFile(uploadFile,board);
					  }
					
		 
		           System.out.println("<h1>파일 업로드 완료</h1>");
		 
		 
			        } catch (Exception e) {
			            // 파일 업로드 처리 중 오류가 발생하는 경우
			            e.printStackTrace();
			           System.out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
			        }
				}
			
				
				
				boardService = BoardService.getInstance();
				
				count = boardService.getBoardsCount(search);
				pagination = new Pagination(page, count, search);
				boardlist = boardService.getBoards(pagination);			
				request.setAttribute("board", boardlist);
				request.setAttribute("pagination", pagination);	
				
				view = "board/insert-result";
				break;
				
			case "/board-detail.do":
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService1 = BoardService.getInstance();
				board = boardService1.detailBoard(b_idx);
				
				
				String reqPage5 = request.getParameter("page");
				if(reqPage5 != null) {
					page = Integer.parseInt(reqPage5);
				}
			
				strType = request.getParameter("type");
				if (strType != null && !strType.equals("")) {
					type = Integer.parseInt(request.getParameter("type"));
					keyword = request.getParameter("keyword");
					search = new Search(type, keyword);
				}
				
				commentService = CommentService.getInstance();
				count = commentService.getCommentsCount(search, board);
				pagination = new Pagination(page,count,search);
				ArrayList<Comment> list5 = commentService.getComments(pagination, board);
				
				
							
				
				
//				request.setAttribute("file", uploadedFiles);
				request.setAttribute("list", list5);
				request.setAttribute("pagination", pagination);	
				request.setAttribute("board", board);
				view = "board/detail";
				
				break;
				
			case "/board-edit.do":
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService2 = BoardService.getInstance();
				boardVo = boardService2.editBoard(b_idx);
				view = "board/edit";
				request.setAttribute("board", boardVo);
				break;
				
			case "/board-edit-process.do":
				
				Board boardvo = new Board();
				boardvo.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				boardvo.setB_title(request.getParameter("title"));
				boardvo.setB_content(request.getParameter("content"));
				boardvo.setB_writer(request.getParameter("writer"));
				boardvo.setB_date(sDate.format(new Date()));
//				boardvo.setB_view(Integer.parseInt(request.getParameter("view")));
				boardService = BoardService.getInstance();
				boardService.editProcessBoard(boardvo);
				
				view = "board/edit-process";
				break;
				
			case "/board-delete.do":
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService3 = BoardService.getInstance();
				boardVo = boardService3.deleteBoard(b_idx);
				view = "board/boardDelete";
				break;
				
			case "/update-auth.do":
				user = new User();
				user.setU_auth(request.getParameter("auth"));
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				
				userSvc = UserService.getInstance();
				userSvc.updateAuth(user);
				// /user-list.do 로 리다이렉트
				view = "user-list.do";
				isRedirected = true;
				break;
							
			case "/comment-list.do":
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService10 = BoardService.getInstance();
				board = boardService10.detailBoard(b_idx);
				
				String reqPage6 = request.getParameter("page");
				if(reqPage6 != null) {
					page = Integer.parseInt(reqPage6);
				}
			
				strType = request.getParameter("type");
				if (strType != null && !strType.equals("")) {
					type = Integer.parseInt(request.getParameter("type"));
					keyword = request.getParameter("keyword");
					search = new Search(type, keyword);
				}
				
				commentService = CommentService.getInstance();
				count = commentService.getCommentsCount(search,board);
				pagination = new Pagination(page,count,search);
				ArrayList<Comment> list6 = commentService.getComments(pagination, board);
				
				request.setAttribute("list", list6);
				request.setAttribute("pagination", pagination);
				request.setAttribute("board", board);
				view = "comment/aj-list";
				break;
			
			case "/comment-insert.do":
				session = request.getSession();
				user = (User)session.getAttribute("user");
				comment = new Comment();
				comment.setU_idx(user.getU_idx());
				comment.setC_content(request.getParameter("c_content"));
//				comment.setC_group(0);
//				comment.setC_order(0);
//				comment.setC_depth(0);
				comment.setC_group(Integer.parseInt(request.getParameter("c_group")));
				comment.setC_order(Integer.parseInt(request.getParameter("c_order")));
				comment.setC_depth(Integer.parseInt(request.getParameter("c_depth")));
				comment.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				comment.setC_date(sDate.format(new Date()));

				CommentService commentService1 = CommentService.getInstance();
				commentService1.insertComment(comment);
	
				board.setB_idx(comment.getB_idx());
				count = commentService1.getCommentsCount(search,board);
				pagination = new Pagination(page, count, search);
				ArrayList<Comment> list7 = commentService1.getComments(pagination,board);			
				request.setAttribute("list", list7);
				request.setAttribute("pagination", pagination);	
				
				view = "comment/aj-list";
//				isRedirected = true;
				break;

			case "/comment-delete.do":
		
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService11 = BoardService.getInstance();
				board = boardService11.detailBoard(b_idx);
				
				c_idx = Integer.parseInt(request.getParameter("c_idx"));
				commentService = CommentService.getInstance();
				commentVo = commentService.deleteComment(c_idx);
				
				
				String reqPage9 = request.getParameter("page");
				if(reqPage9 != null) {
					page = Integer.parseInt(reqPage9);
				}
			
				strType = request.getParameter("type");
				if (strType != null && !strType.equals("")) {
					type = Integer.parseInt(request.getParameter("type"));
					keyword = request.getParameter("keyword");
					search = new Search(type, keyword);
				}
				
				commentService = CommentService.getInstance();
				count = commentService.getCommentsCount(search,board);
				pagination = new Pagination(page,count,search);
				ArrayList<Comment> list9 = commentService.getComments(pagination,board);
				
				request.setAttribute("list", list9);
				request.setAttribute("pagination", pagination);
				
				
				view = "comment/aj-list";
				break;
			
			case "/comment-edit.do":
				
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				BoardService boardService12 = BoardService.getInstance();
				board = boardService12.detailBoard(b_idx);
				
				comment = new Comment();
				comment.setC_idx(Integer.parseInt(request.getParameter("c_idx")));
				comment.setC_content(request.getParameter("c_content"));
				comment.setB_idx(comment.getB_idx());
				comment.setC_date(sDate.format(new Date()));
				commentService = CommentService.getInstance();
				commentService.editProcessComment(comment);
				
				String reqPage10 = request.getParameter("page");
				if(reqPage10 != null) {
					page = Integer.parseInt(reqPage10);
				}
			
				strType = request.getParameter("type");
				if (strType != null && !strType.equals("")) {
					type = Integer.parseInt(request.getParameter("type"));
					keyword = request.getParameter("keyword");
					search = new Search(type, keyword);
				}
				
				commentService = CommentService.getInstance();
				
				count = commentService.getCommentsCount(search,board);
				pagination = new Pagination(page,count,search);
				ArrayList<Comment> list10 = commentService.getComments(pagination,board);
				
				request.setAttribute("list", list10);
				request.setAttribute("pagination", pagination);
				
				
				view = "comment/aj-list";
				break;
		
				
//			case "/fileUpload.do":
//				view = "fileUpload/fileUpload";	
//				break;
//		
//			case "/fileUpload-process.do":
//		
//				 String CHARSET = "utf-8";
//				 String ATTACHES_DIR = "C:\\Users\\금재민\\Documents\\workspace-spring-tool-suite-4-4.12.0.RELEASE\\lcomputerstudy2-2.10-apache\\WebContent\\img";
//				 int LIMIT_SIZE_BYTES = 1024 * 1024;
//
//
//			    response.setContentType("text/html; charset=UTF-8");
//		        request.setCharacterEncoding(CHARSET);
//		        PrintWriter out = response.getWriter();
//
//
//		 
//		        File attachesDir = new File(ATTACHES_DIR);
//		 
//		 
//		        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
//		        fileItemFactory.setRepository(attachesDir);
//		        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
//		        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
//		 
//		 
//		        try {
//		            List<FileItem> items = fileUpload.parseRequest(request);
//		            for (FileItem item : items) {
//		                if (item.isFormField()) {
//		                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));
//		                } else {
//		                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
//		                            item.getName(), item.getSize());
//		                    if (item.getSize() > 0) {
//		                        String separator = File.separator;
//		                        int index =  item.getName().lastIndexOf(separator);
//		                        String fileName = item.getName().substring(index  + 1);
//		                        uploadFile = new File(ATTACHES_DIR +  separator + fileName);
//		                        if(uploadFile.exists()) {
//		                        	for(int k=0; true; k++) {
//		                        		uploadFile = new File(ATTACHES_DIR,"("+k+")"+fileName);
//		                        		if(!uploadFile.exists()) {
//		                        			fileName = "("+k+")"+fileName;
//		                        			break;
//		                        			
//		                        		}
//		                        	}
//		                        }item.write(uploadFile);
////		                        
////		                        String fileName = item.getName();
////		                        Date date = new Date();
////		                        SimpleDateFormat today = new SimpleDateFormat("yyyyMMddHHmmss");
////		                        if(new File(ATTACHES_DIR + fileName).exists()) {
////		                        	ATTACHES_DIR += today.format(date) + "/";
////		                        	new File(ATTACHES_DIR).mkdir();
//		                        	  
//		                      
////		                        }
//		                        
//		                    
//		                    }
//		                }
//		            }
//		 
//		 
//		           System.out.println("<h1>파일 업로드 완료</h1>");
//		 
//		 
//		        } catch (Exception e) {
//		            // 파일 업로드 처리 중 오류가 발생하는 경우
//		            e.printStackTrace();
//		           System.out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
//		        }
//		    
//			fileService = FileService.getInstance();
//			fileService.insertFile(uploadFile);
//		        
//		 
//				view = "fileUpload/filesu";
//				
//			
//				break;
		


				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
//				// 1. upload 폴더 생성이 안되어 있으면 생성
//				String saveDirectory = "C:\\Users\\l2-evening\\Documents\\workspace-spring-tool-suite-4-4.7.1.RELEASE\\lcomputerstudy2-.2.3\\WebContent\\img";
//				System.out.println(saveDirectory);
//				
//				File saveDir = new File(saveDirectory);
//				if (!saveDir.exists())
//					saveDir.mkdirs();
//				// 2. 최대크기 설정
//				int maxPostSize = 1024 * 1024 * 5; // 5MB  단위 byte
//
//				//3. 인코딩 방식 설정
//				String encoding = "UTF-8";
//
//				//4. 파일정책, 파일이름 충동시 덮어씌어짐으로 파일이름 뒤에 인덱스를 붙인다.
//			  //a.txt
//				//a1.txt 와 같은 형식으로 저장된다.
//				FileRenamePolicy policy = new DefaultFileRenamePolicy();
//				MultipartRequest mrequest 
//				= new MultipartRequest(request //MultipartRequest를 만들기 위한 request
//						, saveDirectory //저장 위치
//						, maxPostSize //최대크기
//						, encoding //인코딩 타입
//						, policy); //파일 정책
//				
//				String name = mrequest.getParameter("name");
//				File uploadFile = mrequest.getFile("upload[]");
//			  //input type="file" 태그의 name속성값을 이용해 파일객체를 생성
//				long uploadFile_length = uploadFile.length();
//				String originalFileName = mrequest.getOriginalFileName("upload[]"); //기존 이름
//				String filesystemName = mrequest.getFilesystemName("upload[]"); //기존
//		
//				
//				file = new FileUpload();
////				file.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
//				file.setF_name(uploadFile.getName());
//				
//				fileService = FileService.getInstance();
//				fileService.insertFile(file);
//				
//				
//				view = "fileUpload/filesu";
//				
//			
//				break;
				
//			case "/comment-edit-process.do":
//				
//				comment = new Comment();
//				comment.setC_idx(Integer.parseInt(request.getParameter("c_idx")));
//				comment.setC_content(request.getParameter("c_content"));
//				comment.setC_date(sDate.format(new Date()));
//				commentService = CommentService.getInstance();
//				commentService.editProcessComment(comment);
//				
//				view = "comment/aj-list";
//				break;	
					
		}
		
		if (isRedirected) {
			response.sendRedirect(view);
			return;
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(view+".jsp");
		rd.forward(request, response);
		

	}

	String checkSession(HttpServletRequest request, HttpServletResponse response, String command) {
		HttpSession session = request.getSession();
		
		String[] authList = {
//				"/user-list.do"
//				,"/user-insert.do"
//				,"/user-insert-process.do"
//				,"/user-detail.do"
//				,"/user-edit.do"
//				,"/user-edit-process.do"
//				,"/logout.do"
			};
		
		for (String item : authList) {
			if (item.equals(command)) {
				if (session.getAttribute("user") == null) {
					command = "/access-denied.do";
				}
			}
		}
		return command;
	}
	
	

}

