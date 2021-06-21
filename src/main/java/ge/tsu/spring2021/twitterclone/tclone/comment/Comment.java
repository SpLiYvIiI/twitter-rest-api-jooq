package ge.tsu.spring2021.twitterclone.tclone.comment;


import ge.tsu.spring2021.twitterclone.tclone.tweet.Tweet;
import ge.tsu.spring2021.twitterclone.tclone.user.User;

import javax.persistence.*;

public class Comment {
    private long comment_id;
    private String comment_text;
    public Comment(){

    }

    public Comment(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }


    public long getComment_id() {
        return comment_id;
    }
}
