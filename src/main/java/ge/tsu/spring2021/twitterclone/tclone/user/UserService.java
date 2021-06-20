package ge.tsu.spring2021.twitterclone.tclone.user;

import java.util.List;

import ge.tsu.spring2021.twitterclone.tclone.tweet.Tweet;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordAlreadyExistsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserService {
    List<User> getFollowers(String JwtString);
    User getFollower(long id,String JwtString) throws RecordNotFoundException;
    List <User> getFollowing(String JwtString);
    User getFollowingById(long id,String JwtString) throws RecordNotFoundException;
    void followUser(long following,String JwtString) throws RecordNotFoundException, RecordAlreadyExistsException;
    void unfollow(long toUnfollow,String JwtString);
    void editUser(User user , String JwtString);
    List<Tweet> userTimeLine(String JwtString);
}