package com.parent.ws.app.service.impl;

import java.util.ArrayList;

import com.parent.ws.app.persistence.UserEntity;
import com.parent.ws.app.persistence.protocols.UserRepository;
import com.parent.ws.app.service.protocols.UserService;
import com.parent.ws.app.shared.Utils;
import com.parent.ws.app.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final int PUBLIC_USER_ID_LENGHT = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        String userEmail = userDto.getEmail();
        String userPlainTextPassword = userDto.getPassword();

        UserEntity record = userRepository.findByEmail(userEmail);
        if (record != null) {
            throw new RuntimeException("Record Already Exist");
        }

        String userPublicId = utils.generateUserId(PUBLIC_USER_ID_LENGHT);
        String userEncryptedPassword = bCryptPasswordEncoder.encode(userPlainTextPassword);

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setUserId(userPublicId);
        userEntity.setEncryptedPassword(userEncryptedPassword);

        UserEntity persistedUserDetails = userRepository.save(userEntity);
        UserDto userDetailsDto = new UserDto();

        BeanUtils.copyProperties(persistedUserDetails, userDetailsDto);

        return userDetailsDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        String userEmail = userEntity.getEmail();
        String userEncryptedPassword = userEntity.getEncryptedPassword();
        UserDetails userDetails = new User(userEmail, userEncryptedPassword, new ArrayList<>());

        return userDetails;
    }

}
