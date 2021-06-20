package ge.tsu.spring2021.twitterclone.tclone.tweet;

import ge.tsu.spring2021.twitterclone.tclone.user.User;
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
public class TweetServiceImpl implements TweetService{
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    DSLContext dslContext;
    public List<Tweet> getTweets(String JwtToken){
        String userName = jwtUtils.extractUsername(JwtToken);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        return dslContext.select(Tables.TWEETS.TWEET_ID,Tables.TWEETS.TWEET_TITLE,Tables.TWEETS.TWEET_TEXT)
                .from(Tables.TWEETS)
                .where(Tables.TWEETS.USER_ID.equal(userId))
                .fetch()
                .into(Tweet.class);
    }
    public Tweet getTweet(long id,String JwtToken) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtToken);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c = dslContext.selectCount()
                .from(Tables.TWEETS)
                .where(Tables.TWEETS.TWEET_ID.equal((int) id))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Tweet with such id doesn't exist");
        return dslContext.select(Tables.TWEETS.TWEET_ID,Tables.TWEETS.TWEET_TITLE,Tables.TWEETS.TWEET_TEXT)
                .from(Tables.TWEETS)
                .where(Tables.TWEETS.USER_ID.equal(userId))
                .and(Tables.TWEETS.TWEET_ID.equal((int) id))
                .fetchOne()
                .into(Tweet.class);
    }
    public void postTweet(Tweet newTweet,String JwtString){
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        dslContext.insertInto(
                Tables.TWEETS,
                Tables.TWEETS.TWEET_TITLE,
                Tables.TWEETS.TWEET_TEXT,
                Tables.TWEETS.USER_ID
        ).values(
                newTweet.getTweet_title(),
                newTweet.getTweet_text(),
                userId
        ).execute();
    }
    public void deleteTweet(long id, String JwtString){
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        dslContext.delete(Tables.TWEETS)
                .where(Tables.TWEETS.TWEET_ID.equal((int) id))
                .and(Tables.TWEETS.USER_ID.equal(userId))
                .execute();
    }

    public void editTweet(long id,Tweet newTweet, String JwtString) throws RecordNotFoundException {
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c = dslContext.selectCount()
                .from(Tables.TWEETS)
                .where(Tables.TWEETS.TWEET_ID.equal((int) id))
                .fetchOne()
                .into(int.class);
        if(c == 0) throw new RecordNotFoundException("Tweet with such id doesn't exist");
        dslContext.update(Tables.TWEETS)
                .set(Tables.TWEETS.TWEET_TITLE, newTweet.getTweet_title())
                .set(Tables.TWEETS.TWEET_TEXT, newTweet.getTweet_text())
                .where(Tables.TWEETS.TWEET_ID.equal((int) id))
                .and(Tables.TWEETS.USER_ID.equal(userId))
                .execute();
    }
}
