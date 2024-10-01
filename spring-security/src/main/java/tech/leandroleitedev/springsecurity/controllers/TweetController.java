package tech.leandroleitedev.springsecurity.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.leandroleitedev.springsecurity.controllers.dtos.FeedItemResponse;
import tech.leandroleitedev.springsecurity.controllers.dtos.FeedTweetsResponse;
import tech.leandroleitedev.springsecurity.controllers.dtos.TweetRequest;
import tech.leandroleitedev.springsecurity.entitys.EnumRoleName;
import tech.leandroleitedev.springsecurity.entitys.Tweet;
import tech.leandroleitedev.springsecurity.repositorys.TweetRepository;
import tech.leandroleitedev.springsecurity.repositorys.UserRepository;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody @Valid TweetRequest tweetRequest, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(tweetRequest.content());
        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getRoleName().equals(EnumRoleName.ADMIN));

        var tweet = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.delete(tweet);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedTweetsResponse> feedTweetsResponseResponseEntity(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        var tweets = tweetRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimesTamp"))
                .map(tweet ->
                        new FeedItemResponse(
                                tweet.getTweetId(),
                                tweet.getContent(),
                                tweet.getUser().getUsername()));

        return ResponseEntity.ok(new FeedTweetsResponse(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    }

}
