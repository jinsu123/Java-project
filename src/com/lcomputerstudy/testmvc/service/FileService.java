package com.lcomputerstudy.testmvc.service;

import com.lcomputerstudy.testmvc.dao.BoardDAO;
import com.lcomputerstudy.testmvc.dao.FileDAO;
import com.lcomputerstudy.testmvc.vo.FileUpload;

public class FileService {

	private static FileService service = null;
	private static FileDAO dao = null;
	
	private FileService() {
		
	}
	
	public static FileService getInstance() {
		if(service == null) {
			service = new FileService();
			dao = FileDAO.getInstance();
		}
		return service;
	}
	
	
	public void insertFile(FileUpload fileupload) {
		dao.insertFile(fileupload);
	}
}