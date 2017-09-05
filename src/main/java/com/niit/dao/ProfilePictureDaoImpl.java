package com.niit.dao;

import java.util.List;


import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.niit.model.ProfilePicture;

@Repository
@Transactional
public class ProfilePictureDaoImpl implements ProfilePictureDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void save(ProfilePicture profilePicture) {		
		Session session=sessionFactory.openSession();
		session.saveOrUpdate(profilePicture);
		session.flush();
		session.close();
	}
	
	/*public ProfilePicture uploadProfilePic()*/
	
	public ProfilePicture getProfilePic(String username) {
		Session session=sessionFactory.openSession();
		//select * from profilepicture where username='admin'
		ProfilePicture profilePicture=(ProfilePicture)session.get(ProfilePicture.class, username);
		session.close();
		return profilePicture;
	}

	

	
	
}