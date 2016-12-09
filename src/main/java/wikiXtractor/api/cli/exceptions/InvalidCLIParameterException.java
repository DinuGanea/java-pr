package wikiXtractor.api.cli.exceptions;


public class InvalidCLIParameterException extends Exception {

    public InvalidCLIParameterException(String message) {
        super(message);
    }

    public InvalidCLIParameterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
