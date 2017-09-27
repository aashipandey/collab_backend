package com.niit.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.model.BlogComment;
import com.niit.model.BlogPost;

@Repository
@Transactional
public class BlogPostDaoImpl implements BlogPostDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveBlogPost(BlogPost blogPost) {		
		Session session=sessionFactory.openSession();
		session.save(blogPost);
		session.flush();
	}
	
	public List<BlogPost> getBlogPosts(int approved){
		Session session=sessionFactory.getCurrentSession();
		//if approved method parameter is 0 => select * from blogpost where approved=0;[waiting for approval]
		//if approved parameter is 1 => select * from blogpost where approved=1;[approved blogposts]
		Query query=session.createQuery("from BlogPost where approved="+approved);
		return query.list();
		
	}
	
	public BlogPost getBlogPostById(int id){
		Session session=sessionFactory.getCurrentSession();
		BlogPost blogPost=(BlogPost)session.get(BlogPost.class,id);
		return blogPost;
	}


	public void updateBlogPost(BlogPost blogPost) {
		Session session=sessionFactory.getCurrentSession();
		System.out.println(blogPost.isApproved());
		session.update(blogPost);//update blogpost set approved=1 wehere id=248
		System.out.println(blogPost.isApproved());
		
	}


	public void addBlogcomment(BlogComment blogComment)
	{
		Session session=sessionFactory.getCurrentSession();
		session.save(blogComment);//insert into blogcomment
	}


	public List<BlogComment> getAllBlogComments(int blogPostId) 
	{
		Session session=sessionFactory.getCurrentSession();
		//select * from blogcomment where blogpost_id=246
		Query query=session.createQuery("from BlogComment where blogPost.id=?");
		query.setInteger(0,  blogPostId);
		return query.list();
	}

	
}