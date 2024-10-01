package tech.leandroleitedev.springsecurity.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank(message = "Set your username") String username,
        @NotBlank(message = "Set your password") String password){}
