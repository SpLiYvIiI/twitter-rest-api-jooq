package ge.tsu.spring2021.twitterclone.tclone.utils.exceptions;

public class RecordNotFoundException extends Exception{
    public RecordNotFoundException() {
    }

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
