package com.mobile.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobile.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	//per Spring Data JPA query methods start with findBy<field in UserEntity>, field must match database table fields
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	//UserEntity findByLastName(String lastName);
}
