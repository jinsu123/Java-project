package com.lcomputerstudy.testmvc.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Board {
	
	private int b_idx;
	private String b_title;
	private String b_content;
	private String b_writer;
	private String b_date;
	private int b_view;
	private int rownum;
	private int u_idx;
	private User user;

	private Pagination pagination;
	private int b_group;
	private int b_order;
	private int b_depth;
	
	

	public int getB_idx() {
		return b_idx;
	}
	
	public void setB_idx(int b_idx) {
		this.b_idx = b_idx;
	}
	
	public String getB_title() {
		return b_title;
	}
	
	public void setB_title(String b_title) {
		this.b_title = b_title;
	}
	
	public String getB_content() {
		return b_content;
	}
	
	public void setB_content(String b_content) {
		this.b_content = b_content;
	}
	
	public String getB_writer() {
		return b_writer;
	}
	
	public void setB_writer(String b_writer) {
		this.b_writer = b_writer;
	}
	
	public String getB_date() {
		return b_date;
	}
	
	public void setB_date(String b_date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = df.parse(b_date);
			this.b_date = df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			this.b_date = b_date;
		}
		
	}
	
	public int getB_view() {
		return b_view;
	}
	
	public void setB_view(int b_view) {
		this.b_view = b_view;
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
	
	public int getB_group() {
		return b_group;
	}
	
	public void setB_group(int b_group) {
		this.b_group = b_group;
	}
	
	public int getB_order() {
		return b_order;
	}
	
	public void setB_order(int b_order) {
		this.b_order = b_order;
	}
	
	public int getB_depth() {
		return b_depth;
	}
	
	public void setB_depth(int b_depth) {
		this.b_depth = b_depth;
	}
	

}
