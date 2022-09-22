package terafintech.terabank.exception;

public class InvalidRemitIdException extends NumberFormatException {

    public InvalidRemitIdException() {
        super();
    }

    public InvalidRemitIdException(String message) {
        super(message);
    }

}
