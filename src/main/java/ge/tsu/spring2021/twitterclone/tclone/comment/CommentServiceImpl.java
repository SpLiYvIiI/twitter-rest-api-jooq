package ge.tsu.spring2021.twitterclone.tclone.comment;

import ge.tsu.spring2021.twitterclone.tclone.user.UserService;
import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import nu.studer.sample.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    DSLContext dslContext;
    @Autowired
    private JwtUtils jwtUtils;

    public List<Comment> getComments(long id,String JwtToken){
        return dslContext.select(Tables.COMMENTS.COMMENT_ID,Tables.COMMENTS.COMMENT_TEXT)
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(id))
                .fetch()
                .into(Comment.class);
    }
    public Comment getComment(long tweetId,long commentId,String JwtToken) throws RecordNotFoundException{
        int c = dslContext.selectCount()
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(tweetId))
                .and(Tables.COMMENTS.COMMENT_ID.equal((int) commentId))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Comment with such comment or tweet id doesn't exist");
        return dslContext.select(Tables.COMMENTS.COMMENT_ID,Tables.COMMENTS.COMMENT_TEXT)
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(tweetId))
                .and(Tables.COMMENTS.COMMENT_ID.equal((int) commentId))
                .fetchOne()
                .into(Comment.class);
    }

    public void postComment(long id,Comment comment,String JwtToken) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtToken);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c = dslContext.selectCount()
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(id))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Tweet with such id doesn't exist");
        dslContext.insertInto(
                Tables.COMMENTS,
                Tables.COMMENTS.COMMENT_TEXT,
                Tables.COMMENTS.TWEET_ID,
                Tables.COMMENTS.USER_ID
        ).values(
                comment.getComment_text(),
                id,
                userId
        ).execute();
    }
    public void deleteComment(long tweetId, long commentId, String JwtToken) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtToken);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c = dslContext.selectCount()
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(tweetId))
                .and(Tables.COMMENTS.USER_ID.equal(userId))
                .and(Tables.COMMENTS.COMMENT_ID.equal((int) commentId))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Something went wrong");
        dslContext.delete(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(tweetId))
                .and(Tables.COMMENTS.COMMENT_ID.equal((int) commentId))
                .and(Tables.COMMENTS.USER_ID.equal(userId))
                .execute();
    }
    public void editComment(long tweetId,long commentId,Comment newComment,String JwtToken) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtToken);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c = dslContext.selectCount()
                .from(Tables.COMMENTS)
                .where(Tables.COMMENTS.TWEET_ID.equal(tweetId))
                .and(Tables.COMMENTS.USER_ID.equal(userId))
                .and(Tables.COMMENTS.COMMENT_ID.equal((int) commentId))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Something went wrong");
        dslContext.update(Tables.COMMENTS)
                .set(Tables.COMMENTS.COMMENT_TEXT, newComment.getComment_text())
                .execute();
    }
}
