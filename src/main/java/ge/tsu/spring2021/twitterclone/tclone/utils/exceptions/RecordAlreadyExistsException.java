package ge.tsu.spring2021.twitterclone.tclone.utils.exceptions;

public class RecordAlreadyExistsException extends Exception{
    public RecordAlreadyExistsException(String message){
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
