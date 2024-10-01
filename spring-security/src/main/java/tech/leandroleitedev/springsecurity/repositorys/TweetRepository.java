package tech.leandroleitedev.springsecurity.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.leandroleitedev.springsecurity.entitys.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {


}
