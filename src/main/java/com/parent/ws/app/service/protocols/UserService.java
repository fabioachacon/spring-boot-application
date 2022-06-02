package com.parent.ws.app.service.protocols;

import java.util.List;

import com.parent.ws.app.shared.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(String userId, UserDto userDto);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);
}
