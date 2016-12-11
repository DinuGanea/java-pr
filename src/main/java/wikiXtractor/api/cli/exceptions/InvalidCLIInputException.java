package wikiXtractor.api.cli.exceptions;

/**
 * Define a personalised exception for cli commands
 *
 * Thrown if the input isn't recognized, or doesn't match the pattern
 *
 * @author Sonia Rooshenas
 */
public class InvalidCLIInputException extends Exception {

    public InvalidCLIInputException(String message) {
        super(message);
    }

    public InvalidCLIInputException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
