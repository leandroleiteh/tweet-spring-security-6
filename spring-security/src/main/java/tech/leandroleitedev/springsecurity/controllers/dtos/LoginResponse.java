package tech.leandroleitedev.springsecurity.controllers.dtos;

public record LoginResponse(String accessToken, String expiresIn) {
}
