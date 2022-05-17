package com.parent.ws.app.persistence.protocols;

import com.parent.ws.app.persistence.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
