package terafintech.terabank.exception;

public class NotEnoughMoneyException extends NumberFormatException {

    public NotEnoughMoneyException() {
        super();
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }

}
