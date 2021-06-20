package ge.tsu.spring2021.twitterclone.tclone.tweet;

import java.util.List;

import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TweetService {
    List<Tweet> getTweets(String JwtToken);
    Tweet getTweet(long id,String JwtToken) throws RecordNotFoundException;
    void postTweet(Tweet newTweet,String JwtString);
    void deleteTweet(long id, String JwtString);
    void editTweet(long id,Tweet newTweet, String JwtString) throws RecordNotFoundException;
}