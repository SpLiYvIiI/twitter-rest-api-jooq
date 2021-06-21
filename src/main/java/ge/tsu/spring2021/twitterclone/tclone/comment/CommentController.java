package ge.tsu.spring2021.twitterclone.tclone.comment;

import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/api/tweets/{id}/comments")
    public List<Comment> getComments(@PathVariable long id, HttpServletRequest request){
        return commentService.getComments(id,jwtUtils.getJwtString(request.getCookies()));
    }
    @GetMapping("/api/tweets/{tweetId}/comments/{commentId}")
    public Comment getComment(@PathVariable long tweetId, @PathVariable long commentId, HttpServletRequest request) throws RecordNotFoundException {
        return commentService.getComment(tweetId,commentId,jwtUtils.getJwtString(request.getCookies()));
    }
    @PostMapping("/api/tweets/{id}/comments")
    public void postComment(@PathVariable long id, @RequestBody Comment comment, HttpServletRequest request) throws RecordNotFoundException{
        commentService.postComment(id,comment,jwtUtils.getJwtString(request.getCookies()));
    }
    @DeleteMapping("/api/tweets/{tweetId}/comments/{commentId}")
    public void deleteComment(@PathVariable long tweetId,@PathVariable long commentId, HttpServletRequest request) throws RecordNotFoundException{
        commentService.deleteComment(tweetId,commentId,jwtUtils.getJwtString(request.getCookies()));
    }
    @PutMapping("/api/tweets/{tweetId}/comments/{commentId}")
    public void editComment(@PathVariable long tweetId, @PathVariable long commentId,@RequestBody Comment newComment, HttpServletRequest request) throws RecordNotFoundException{
        commentService.editComment(tweetId,commentId,newComment,jwtUtils.getJwtString(request.getCookies()));
    }
}
