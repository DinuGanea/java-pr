package wikiXtractor.api.cli.exceptions;

public class InvalidCLIInputException extends Exception {

    public InvalidCLIInputException(String message) {
        super(message);
    }

    public InvalidCLIInputException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
