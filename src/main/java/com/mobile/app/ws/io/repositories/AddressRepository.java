package com.mobile.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobile.app.ws.io.entity.AddressEntity;
import com.mobile.app.ws.io.entity.UserEntity;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
}
