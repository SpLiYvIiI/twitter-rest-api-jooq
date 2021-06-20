package ge.tsu.spring2021.twitterclone.tclone.comment;

import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(long id, String JwtToken);
    Comment getComment(long tweetId,long commentId,String JwtToken) throws RecordNotFoundException;
    void postComment(long id,Comment comment,String JwtToken) throws RecordNotFoundException;
    void deleteComment(long tweetId, long commentId, String JwtToken) throws RecordNotFoundException;
    void editComment(long tweetId,long commentId,Comment newComment,String JwtToken) throws RecordNotFoundException;
}
