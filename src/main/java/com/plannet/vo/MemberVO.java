package com.plannet.vo;

import java.sql.Date;

public class MemberVO {
	private String id;
	private String pwd;
	private String name;
	private String nickname;
	private String email;
	private String tel;
//	private Date birth;//
	private Date join;
	private String memo;
	private String sns;
	private String profile;
	private String img;
	private boolean isReg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
//	public Date getBirth() {
//		return birth;
//	}
//	public void setBirth(Date birth) {
//		this.birth = birth; //
//	}
	public Date getJoin() {
		return join;
	}
	public void setJoin(Date join) {
		this.join = join;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getSns() {
		return sns;
	}
	public void setSns(String sns) {
		this.sns = sns;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public boolean isReg() {
		return isReg;
	}
	public void setReg(boolean isReg) {
		this.isReg = isReg;
	}
	
	
}

