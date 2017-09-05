package com.niit.dao;

import java.util.List;


import com.niit.model.ProfilePicture;

public interface ProfilePictureDao {
	
	void save(ProfilePicture profilePicture);
	ProfilePicture getProfilePic(String username);

}