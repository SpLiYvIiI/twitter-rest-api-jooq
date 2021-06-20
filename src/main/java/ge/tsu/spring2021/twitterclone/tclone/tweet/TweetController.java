package ge.tsu.spring2021.twitterclone.tclone.tweet;


import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TweetController {
    @Autowired
    private TweetService tweetService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/api/tweets")
    public List<Tweet> getTweets(HttpServletRequest request)
    {
        return tweetService.getTweets(jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/tweets/{id}")
    public Tweet getTweet(@PathVariable long id,HttpServletRequest request) throws RecordNotFoundException{
        return tweetService.getTweet(id,jwtUtils.getJwtString(request.getCookies()));
    }

    @PostMapping("/api/tweets")
    public void postTweets(@RequestBody Tweet newTweet, HttpServletRequest request){
        tweetService.postTweet(newTweet,jwtUtils.getJwtString(request.getCookies()));
    }
    @DeleteMapping("/api/tweets/{id}")
    public void deleteTweet(@PathVariable long id, HttpServletRequest request){
        tweetService.deleteTweet(id,jwtUtils.getJwtString(request.getCookies()));
    }
    @PutMapping("/api/tweets/{id}")
    public void editTweet(@RequestBody Tweet newTweet, @PathVariable long id, HttpServletRequest request) throws RecordNotFoundException{
        tweetService.editTweet(id,newTweet,jwtUtils.getJwtString(request.getCookies()));
    }

}
