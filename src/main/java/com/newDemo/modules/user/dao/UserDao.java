package com.newDemo.modules.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.newDemo.common.BaseDao;
import com.newDemo.modules.user.domain.User;

@Service
public class UserDao extends BaseDao{

@Autowired MongoTemplate mongoTemplate;
	
	public User findByUserName(String username){
		return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), User.class);
	}
	
	public User findByEmail(String email){
		return mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class);
	}
	
	public User findUserById(String id){
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
	}
	
	public List<User> getUsers(String userId){
		Query query = new Query(Criteria.where("_id").ne(userId));
		return mongoTemplate.find(query, User.class);
	}
}