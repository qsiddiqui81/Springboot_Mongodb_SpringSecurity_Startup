package com.newDemo.common;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.WriteResult;

public class BaseDao{
	
	@Autowired MongoTemplate mongoTemplate;
	
	public BaseEntity save(BaseEntity entity){
		if(entity.getId() == null){
			entity.setCreatedDate(new Date());
		}
		entity.setUpdatedDate(new Date());
		mongoTemplate.save(entity);
		return entity;
	}
	
	public WriteResult delete(BaseEntity entity){
		return mongoTemplate.remove(entity);
	}
}