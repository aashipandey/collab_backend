package com.niit.dao;

import com.niit.model.User;

public interface UserDao {
	
	void registerUser(User user);
	User validateUsername(String username);
	User validateEmail(String email);
	User login(User user);
	void updateUser(User user);
	User getUserByUsername(String username);
	
}