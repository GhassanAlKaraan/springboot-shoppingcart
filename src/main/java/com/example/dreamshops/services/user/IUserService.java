package com.example.dreamshops.services.user;

import com.example.dreamshops.dto.UserDto;
import com.example.dreamshops.models.User;
import com.example.dreamshops.requests.CreateUserRequest;
import com.example.dreamshops.requests.UpdateUserRequest;

public interface IUserService {
  User getUserById(Long UserId);

  User createUser(CreateUserRequest request);

  User updateUser(UpdateUserRequest request, Long userId);

  void deleteUser(Long userId);

  UserDto convertUserToDto(User user);
}
