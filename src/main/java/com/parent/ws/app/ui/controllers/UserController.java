package com.parent.ws.app.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import com.parent.ws.app.constants.ErrorMessages;
import com.parent.ws.app.constants.RequestOperationName;
import com.parent.ws.app.constants.RequestOperationStatus;
import com.parent.ws.app.exceptions.UserServiceException;
import com.parent.ws.app.service.protocols.UserService;
import com.parent.ws.app.shared.dto.UserDto;
import com.parent.ws.app.ui.models.request.UserDetailsRequestModel;
import com.parent.ws.app.ui.models.response.OperationStatusModel;
import com.parent.ws.app.ui.models.response.UserDetailsResponseModel;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}")
    public UserDetailsResponseModel getUser(@PathVariable String id) {
        UserDetailsResponseModel user = new UserDetailsResponseModel();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, user);

        return user;

    }

    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDetailsResponseModel restResponse = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto updatedUser = userService.createUser(userDto);

        BeanUtils.copyProperties(updatedUser, restResponse);

        return restResponse;
    }

    @PutMapping(path = "/{id}")
    public UserDetailsResponseModel updateUser(@PathVariable String id,
            @RequestBody UserDetailsRequestModel userDetails) {
        UserDetailsResponseModel restResponse = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.updateUser(id, userDto);

        BeanUtils.copyProperties(createdUser, restResponse);

        return restResponse;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel status = new OperationStatusModel();
        status.setOperationName(RequestOperationName.DELETE.name());

        try {
            userService.deleteUser(id);
            status.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } catch (Exception e) {
            status.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return status;
    }

    @GetMapping
    public List<UserDetailsResponseModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserDetailsResponseModel> users = new ArrayList<UserDetailsResponseModel>();
        List<UserDto> usersList = userService.getUsers(page, limit);

        usersList.forEach(user -> {
            UserDetailsResponseModel userModel = new UserDetailsResponseModel();
            BeanUtils.copyProperties(user, userModel);
            users.add(userModel);
        });

        return users;
    }

}
