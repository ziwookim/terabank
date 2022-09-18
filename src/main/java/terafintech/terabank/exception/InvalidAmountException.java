package terafintech.terabank.exception;

public class InvalidAmountException extends NumberFormatException {

    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }

}
