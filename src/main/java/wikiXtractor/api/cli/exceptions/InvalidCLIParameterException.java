package wikiXtractor.api.cli.exceptions;


/**
 * Define a personalised exception for cli commands
 *
 * Thrown if entered parameter doesn't match the requirements
 *
 * @author Sonia Rooshenas
 */
public class InvalidCLIParameterException extends Exception {

    public InvalidCLIParameterException(String message) {
        super(message);
    }

    public InvalidCLIParameterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
