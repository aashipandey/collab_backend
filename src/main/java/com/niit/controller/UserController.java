package com.niit.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.niit.dao.UserDao;
import com.niit.model.User;
import com.niit.model.Error;

@Controller
public class UserController {
	
	@Autowired
	private UserDao userDao;
	//?=any type

	@RequestMapping(value="/registeruser",method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		try{
			User duplicateUser=userDao.validateUsername(user.getUsername());
			if(duplicateUser!=null){
				Error error=new Error(2,"Username already exists..please enter different username ");
				return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
			}
			
			User duplicateEmail=userDao.validateEmail(user.getEmail());
			if(duplicateEmail!=null){
				Error error=new Error(3,"Email address already exists..please enter different email address ");
				return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
			}
			
			userDao.registerUser(user);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		catch(Exception e)
		{
			Error error=new Error(1,"Unable to register user details "+e.getMessage());
			return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(value="/login",method=RequestMethod.POST)
	//HttpRequest Body: {"username":"adam","password":"123","firstname":"","lastname":"","email":"","online":"false"}
	public ResponseEntity<?> login(@RequestBody User user,HttpSession session)
	{
		User validuser=userDao.login(user);
		//session.setAttribute("user", validuser);
		if(validuser==null){ //invalid credentials
			Error error=new Error(4,"Invalid Username/Password.. please enter valid username/pswd");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		validuser.setOnline(true);
		userDao.updateUser(validuser); //update only the online status from 0 to 1
		session.setAttribute("username",validuser);
		
		return new ResponseEntity<User>(validuser,HttpStatus.OK);
	}

	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public ResponseEntity<?> logout(HttpSession session){
		
		if(session.getAttribute("username")==null){
    		Error error=new Error(5,"UnAuthorized User");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		String username=(String)session.getAttribute("username");
		User user=userDao.getUserByUsername(username);
		user.setOnline(false);
		userDao.updateUser(user);
		session.removeAttribute("username");
		session.invalidate();
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value="/getuser",method=RequestMethod.GET)
	public ResponseEntity<?> getUser(HttpSession session){
		if(session.getAttribute("username")==null){
    		Error error=new Error(5,"UnAuthorized User");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		String username=(String)session.getAttribute("username");
		User user=userDao.getUserByUsername(username);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateuser",method=RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user,HttpSession session){
		if(session.getAttribute("username")==null){
    		Error error=new Error(5,"UnAuthorized User");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		try{
		userDao.updateUser(user);
		return new ResponseEntity<Void>(HttpStatus.OK);
		}catch(Exception e){
			Error error=new Error(6,"Unable to edit user profile "+e.getMessage());
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
	}

}