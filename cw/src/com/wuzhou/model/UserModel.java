package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class UserModel extends Model<UserModel> {
	public final static UserModel dao = new UserModel();
	
	public UserModel login(String userName, String pwd) {
		return findFirst("select * from wz_user where user_name = ? and user_pwd = ?", userName, pwd);
	}
	
	public List<UserModel> getUserList() {
		return this.find("select user_id, nick_name from wz_user where status=1");
	}
}
