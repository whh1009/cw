package com.wuzhou.service;

import java.util.List;

import com.wuzhou.model.UserModel;

public class UserService {
	
	public UserModel login(String userName, String pwd) {
		return UserModel.dao.login(userName, pwd);
	}
	
	
	public List<UserModel> getUserList() {
		return UserModel.dao.getUserList();
	}
}
