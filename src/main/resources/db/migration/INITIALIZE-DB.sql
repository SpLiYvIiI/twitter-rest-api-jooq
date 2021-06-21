DROP TABLE IF EXISTS customer_following;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS tweets;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       user_id serial PRIMARY KEY,
                       user_name VARCHAR ( 250 ) UNIQUE NOT NULL,
                       password VARCHAR ( 250 ) NOT NULL,
                       email VARCHAR ( 255 ) UNIQUE NOT NULL
);

CREATE TABLE tweets (
                        tweet_id serial PRIMARY KEY,
                        tweet_text VARCHAR ( 250 ) NOT NULL,
                        tweet_title VARCHAR ( 250 ) NOT NULL,
                        user_id BIGINT NOT NULL,
                        FOREIGN KEY(user_id) REFERENCES users (user_id)
);

CREATE TABLE customer_following (
                                    customer_id BIGINT NOT NULL,
                                    following_id BIGINT NOT NULL,
                                    FOREIGN KEY (customer_id)
                                        REFERENCES users (user_id),
                                    FOREIGN KEY (following_id)
                                        REFERENCES users (user_id)
);

CREATE TABLE comments (
                          comment_id serial PRIMARY KEY,
                          comment_text VARCHAR(250) NOT NULL,
                          tweet_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          FOREIGN KEY (tweet_id)
                              REFERENCES tweets(tweet_id),
                          FOREIGN KEY (user_id)
                              REFERENCES users(user_id)
);
