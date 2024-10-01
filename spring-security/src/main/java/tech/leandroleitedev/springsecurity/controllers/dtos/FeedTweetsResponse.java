package tech.leandroleitedev.springsecurity.controllers.dtos;

import java.util.List;

public record FeedTweetsResponse(
        List<FeedItemResponse> feedResponse,
        int page,
        int pageSize,
        int totalPages,
        Long totalElements
) {
}
