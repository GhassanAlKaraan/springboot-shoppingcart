package com.example.dreamshops.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamshops.dto.UserDto;
import com.example.dreamshops.exceptions.AlreadyExistException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.models.User;
import com.example.dreamshops.requests.CreateUserRequest;
import com.example.dreamshops.requests.UpdateUserRequest;
import com.example.dreamshops.responses.ApiResponse;
import com.example.dreamshops.services.user.IUserService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final IUserService iUserService;

  @GetMapping("/{userId}/user")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable(name = "userId") Long userId) {
    try {
      User user = iUserService.getUserById(userId);
      UserDto userDto = iUserService.convertUserToDto(user);
      return ResponseEntity.ok(new ApiResponse("Success", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
    try {
      User user = iUserService.createUser(request);
      UserDto userDto = iUserService.convertUserToDto(user);
      return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
    } catch (AlreadyExistException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/{userId}/update")
  public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,
      @PathVariable(name = "userId") Long userId) {
    try {
      User user = iUserService.updateUser(request, userId);
      UserDto userDto = iUserService.convertUserToDto(user);
      return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{userId}/delete")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    try {
      iUserService.deleteUser(userId);
      return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
