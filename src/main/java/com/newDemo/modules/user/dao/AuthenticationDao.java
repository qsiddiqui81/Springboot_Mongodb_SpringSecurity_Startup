package com.newDemo.modules.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.newDemo.common.BaseDao;
import com.newDemo.modules.user.domain.AuthenticationToken;

@Service
public class AuthenticationDao extends BaseDao{
	
@Autowired MongoTemplate mongoTemplate;
	
	/**
	 * @author prabjot
	 * this method is used to get authentication token object from auth token
	 * @param token
	 * @return
	 */
	public AuthenticationToken findAuthenticationToken(String token){
		return mongoTemplate.findOne(new Query(Criteria.where("token").is(token)), AuthenticationToken.class);
	}
	
	/**
	 * @author prabjot
	 * this method is used to get authentication object by userId
	 * @param userId
	 * @return
	 */
	public AuthenticationToken findByUserId(String userId){
		return mongoTemplate.findOne(new Query(Criteria.where("user").is(userId)), AuthenticationToken.class);
	}
}
