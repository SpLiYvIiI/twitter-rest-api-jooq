package ge.tsu.spring2021.twitterclone.tclone.user;

import ge.tsu.spring2021.twitterclone.tclone.tweet.Tweet;
import ge.tsu.spring2021.twitterclone.tclone.utils.JwtUtils;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordAlreadyExistsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import nu.studer.sample.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    DSLContext dslContext;

    public List<User> getFollowers(String JwtString) {
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        return dslContext.select(Tables.USERS.USER_ID,Tables.USERS.EMAIL, Tables.USERS.USER_NAME )
                .from(Tables.USERS)
                .where(Tables.USERS.USER_ID.in(
                        dslContext.select(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID)
                        .from(Tables.CUSTOMER_FOLLOWING)
                        .where(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID.equal(userId))
                        .fetch()
                        .into(long.class)
                ))
                .fetch()
                .into(User.class);
    }
    public User getFollower(long id,String JwtString) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int count = dslContext
                        .selectCount()
                        .from(Tables.CUSTOMER_FOLLOWING)
                        .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(id))
                        .and(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID.equal(userId))
                        .fetchOne(0, int.class);
        if(count == 0) throw new RecordNotFoundException("Follower with such id doesn't exist");
        return dslContext.select(Tables.USERS.USER_ID,Tables.USERS.USER_NAME, Tables.USERS.EMAIL)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_ID.equal(id))
                .fetchOne()
                .into(User.class);
    }

    public List <User> getFollowing(String JwtString){
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        return dslContext.select(Tables.USERS.USER_ID,Tables.USERS.EMAIL, Tables.USERS.USER_NAME )
                .from(Tables.USERS)
                .where(Tables.USERS.USER_ID.in(
                        dslContext.select(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID)
                                .from(Tables.CUSTOMER_FOLLOWING)
                                .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(userId))
                                .fetch()
                                .into(long.class)
                ))
                .fetch()
                .into(User.class);
    }

    public User getFollowingById(long id,String JwtString) throws RecordNotFoundException{
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int count = dslContext
                .selectCount()
                .from(Tables.CUSTOMER_FOLLOWING)
                .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(userId))
                .and(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID.equal(id))
                .fetchOne(0, int.class);
        if(count == 0) throw new RecordNotFoundException("Followee with such id doesn't exist");
        return dslContext.select(Tables.USERS.USER_ID,Tables.USERS.USER_NAME, Tables.USERS.EMAIL)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_ID.equal(id))
                .fetchOne()
                .into(User.class);
    }

    public void followUser(long following,String JwtString) throws RecordNotFoundException, RecordAlreadyExistsException{
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        int c1 = dslContext
                .selectCount()
                .from(Tables.USERS)
                .where(Tables.USERS.USER_ID.equal(following))
                .fetchOne(0, int.class);
        int c2 = dslContext
                .selectCount()
                .from(Tables.CUSTOMER_FOLLOWING)
                .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(userId))
                .and(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID.equal(following))
                .fetchOne(0,int.class);
        if(c1 == 0) throw new RecordNotFoundException("User with such id doesnt exist");
        if(c2 > 0) throw new RecordAlreadyExistsException("You are already following the user");
        dslContext.insertInto(
                Tables.CUSTOMER_FOLLOWING,
                Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID,
                Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID
                )
                .values(
                        userId,
                        following
                )
                .execute();
    }
    public void unfollow(long toUnfollow,String JwtString){
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        dslContext.delete(Tables.CUSTOMER_FOLLOWING)
                .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(userId))
                .and(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID.equal(toUnfollow))
                .execute();
    }
    public void editUser(User user , String JwtString){
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        dslContext.update(Tables.USERS)
                .set(Tables.USERS.EMAIL, user.getEmail())
                .set(Tables.USERS.USER_NAME, user.getUser_name())
                .set(Tables.USERS.PASSWORD , bCryptPasswordEncoder.encode(user.getPassword()))
                .where(Tables.USERS.USER_ID.equal(userId))
                .execute();
    }
    public List<Tweet> userTimeLine(String JwtString) {
        String userName = jwtUtils.extractUsername(JwtString);
        long userId = dslContext.select(Tables.USERS.USER_ID)
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(userName))
                .fetchOne()
                .into(long.class);
        return dslContext.select(Tables.TWEETS.TWEET_ID,Tables.TWEETS.TWEET_TITLE, Tables.TWEETS.TWEET_TEXT)
                .from(Tables.TWEETS)
                .where(Tables.TWEETS.USER_ID.in(
                        dslContext.select(Tables.CUSTOMER_FOLLOWING.FOLLOWING_ID)
                        .from(Tables.CUSTOMER_FOLLOWING)
                        .where(Tables.CUSTOMER_FOLLOWING.CUSTOMER_ID.equal(userId))
                        .fetch()
                        .into(long.class)
                ))
                 .or(Tables.TWEETS.USER_ID.equal(userId))
                 .fetch()
                 .into(Tweet.class);

    }
}
