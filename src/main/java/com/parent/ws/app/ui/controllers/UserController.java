package com.parent.ws.app.ui.controllers;

import com.parent.ws.app.service.protocols.UserService;
import com.parent.ws.app.shared.dto.UserDto;
import com.parent.ws.app.ui.models.request.UserDetailsRequestModel;
import com.parent.ws.app.ui.models.response.UserRest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUser() {
        return "GET user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest restResponse = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.createUser(userDto);

        BeanUtils.copyProperties(createdUser, restResponse);

        return restResponse;
    }

    @PutMapping
    public String updateUser() {
        return "Update  User";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user";
    }
}
