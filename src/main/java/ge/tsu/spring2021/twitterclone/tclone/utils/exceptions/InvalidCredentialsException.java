package ge.tsu.spring2021.twitterclone.tclone.utils.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException() {
        super();
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
