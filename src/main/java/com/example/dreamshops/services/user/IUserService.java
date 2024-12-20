package com.example.dreamshops.services.user;

import com.example.dreamshops.models.User;
import com.example.dreamshops.requests.CreateUserRequest;
import com.example.dreamshops.requests.UpdateUserRequest;

public interface IUserService {
  User getUserById(Long userId);

  User createUser(CreateUserRequest request);

  User updateUser(UpdateUserRequest request, Long userId);

  void deleteUser(Long userId);
}
