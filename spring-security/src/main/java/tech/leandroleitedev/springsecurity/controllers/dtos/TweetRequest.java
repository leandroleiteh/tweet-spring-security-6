package tech.leandroleitedev.springsecurity.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record TweetRequest(@NotBlank(message = "Typing your message before of send")String content) {
}
