package com.plannet.vo;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class WriteVO {
	String id;
	Date date;
	List<Map<String, Object>> plan;
	String diary;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Map<String, Object>> getPlan() {
		return plan;
	}
	public void setPlan(List<Map<String, Object>> plan) {
		this.plan = plan;
	}
	public String getDiary() {
		return diary;
	}
	public void setDiary(String diary) {
		this.diary = diary;
	}
	
	

}
