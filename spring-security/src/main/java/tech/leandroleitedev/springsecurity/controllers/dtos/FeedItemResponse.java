package tech.leandroleitedev.springsecurity.controllers.dtos;

public record FeedItemResponse(Long tweetId, String content, String username) {
}
