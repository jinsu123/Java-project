package com.lcomputerstudy.testmvc.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
	
	private int c_idx;
	private String c_content;
	private String c_writer;
	private String c_date;
	private int rownum;
	private int u_idx;
	private User user;
	private Pagination pagination;
	private int c_group;
	private int c_order;
	private int c_depth;
	private int b_idx;
	
	

	public int getC_idx() {
		return c_idx;
	}
	
	public void setC_idx(int c_idx) {
		this.c_idx = c_idx;
	}
	
	
	public String getC_content() {
		return c_content;
	}
	
	public void setC_content(String c_content) {
		this.c_content = c_content;
	}
	
	public String getC_writer() {
		return c_writer;
	}
	
	public void setC_writer(String c_writer) {
		this.c_writer = c_writer;
	}
	
	public String getC_date() {
		return c_date;
	}
	
	public void setC_date(String c_date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = df.parse(c_date);
			this.c_date = df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			this.c_date = c_date;
		}
		
	}
	

	public int getRownum() {
		return rownum;
	}
	
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	
	public int getU_idx() {
		return u_idx;
	}

	public void setU_idx(int u_idx) {
		this.u_idx = u_idx;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public int getC_group() {
		return c_group;
	}
	
	public void setC_group(int c_group) {
		this.c_group = c_group;
	}
	
	public int getC_order() {
		return c_order;
	}
	
	public void setC_order(int c_order) {
		this.c_order = c_order;
	}
	
	public int getC_depth() {
		return c_depth;
	}
	
	public void setC_depth(int c_depth) {
		this.c_depth = c_depth;
	}

	public int getB_idx() {
		return b_idx;
	}
	
	public void setB_idx(int b_idx) {
		this.b_idx = b_idx;
	}

	
	
}

	
