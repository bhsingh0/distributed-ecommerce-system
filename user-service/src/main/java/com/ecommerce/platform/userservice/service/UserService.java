package com.ecommerce.platform.userservice.service;

import com.ecommerce.platform.userservice.dto.UserResponse;
import com.ecommerce.platform.userservice.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<UserResponse> getAllUsers();
}