package ge.tsu.spring2021.twitterclone.tclone.user;


import ge.tsu.spring2021.twitterclone.tclone.tweet.Tweet;
import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordAlreadyExistsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/api/users/follower")
    public List <User> getFollower(HttpServletRequest request){
        return userService.getFollowers(jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/users/follower/{id}")
    public User getFollower(@PathVariable long id,HttpServletRequest request) throws RecordNotFoundException {
        return userService.getFollower(id,jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/users/following")
    public List <User> getFollowing(HttpServletRequest request) throws RecordNotFoundException {
        return userService.getFollowing(jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/users/following/{id}")
    public User getFollowingById(@PathVariable long id,HttpServletRequest request) throws RecordNotFoundException {
        return userService.getFollowingById(id,jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/users/timeline")
    public List <Tweet> getTimeline(HttpServletRequest request){
        return userService.userTimeLine(jwtUtils.getJwtString(request.getCookies()));
    }
    @PostMapping("/api/users/follow/{id}")
    public void followUser(@PathVariable long id, HttpServletRequest request) throws RecordNotFoundException, RecordAlreadyExistsException {
        userService.followUser(id,jwtUtils.getJwtString(request.getCookies()));
    }

    @DeleteMapping("/api/users/unfollow/{id}")
    public void unfollow(@PathVariable long id, HttpServletRequest request){
        userService.unfollow(id,jwtUtils.getJwtString(request.getCookies()));
    }
    @PutMapping("/api/users")
    public void editUser(@RequestBody User user,HttpServletRequest request){
        userService.editUser(user,jwtUtils.getJwtString(request.getCookies()));
    }


}
