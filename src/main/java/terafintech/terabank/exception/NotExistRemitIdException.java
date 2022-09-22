package terafintech.terabank.exception;

public class NotExistRemitIdException extends RuntimeException{

    public NotExistRemitIdException() {
        super();
    }

    public NotExistRemitIdException(String message) {
        super(message);
    }

    public NotExistRemitIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistRemitIdException(Throwable cause) {
        super(cause);
    }
}
