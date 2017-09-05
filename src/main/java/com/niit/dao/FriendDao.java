package com.niit.dao;

import java.util.List;

import com.niit.model.User;


public interface FriendDao {
	
	List<User> getListOfSuggestedUsers(String username);

}