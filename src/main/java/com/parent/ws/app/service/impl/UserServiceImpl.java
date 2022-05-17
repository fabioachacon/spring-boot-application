package com.parent.ws.app.service.impl;

import com.parent.ws.app.persistence.UserEntity;
import com.parent.ws.app.persistence.protocols.UserRepository;
import com.parent.ws.app.service.protocols.UserService;
import com.parent.ws.app.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId("testUserId");

        UserEntity persistedUserDetails = userRepository.save(userEntity);
        UserDto persistedUserDetailsDto = new UserDto();

        BeanUtils.copyProperties(persistedUserDetails, persistedUserDetailsDto);

        return persistedUserDetailsDto;
    }

}
