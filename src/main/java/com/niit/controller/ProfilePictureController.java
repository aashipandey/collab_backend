
package com.niit.controller;

import java.util.Date;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import com.niit.dao.ProfilePictureDao;

import com.niit.model.Error;
import com.niit.model.ProfilePicture;
import com.niit.model.User;

@Controller
public class ProfilePictureController {
	@Autowired
	private ProfilePictureDao profilePictureDao;
	
	@RequestMapping(value="/uploadprofilepic",method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePic(@RequestParam CommonsMultipartFile image,HttpSession session){
		User user=(User)session.getAttribute("username");
		
		if(user==null){
			    Error error=new Error(3,"UnAuthorized user");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		} 
		
		User username=(User)session.getAttribute("username");
		ProfilePicture profilePicture=new ProfilePicture();
		profilePicture.setUsername(user.getUsername());
		profilePicture.setImage(image.getBytes());
		profilePictureDao.save(profilePicture);
		return new ResponseEntity<User>(user,HttpStatus.OK);
		}
	
	//http://localhost:8080/backend_project2/getimage/admin
		@RequestMapping(value="/getimage/{username}", method=RequestMethod.GET)
		public @ResponseBody byte[] getProfilePic(@PathVariable String username,HttpSession session){
		
			User user=(User)session.getAttribute("username");
			if(user==null)
				return null;
			else
			{
				ProfilePicture profilePic=profilePictureDao.getProfilePic(username);
				if(profilePic==null)
					return null;
				else
					return profilePic.getImage();
			}
			
	}
}

