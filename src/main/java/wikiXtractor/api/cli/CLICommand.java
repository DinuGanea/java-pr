package wikiXtractor.api.cli;


import com.beust.jcommander.Parameter;
import wikiXtractor.api.Command;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.api.cli.exceptions.InvalidCLIParameterException;
import wikiXtractor.util.Loggable;

import java.util.List;

/**
 * CLI command abstraction that tracks the input and defines methods for every CLI command
 *
 *
 * @param <T> command line command return type
 * @author Dinu Ganea
 */
public abstract class CLICommand<T> implements Command<T>, Loggable {

    /**
     * VERY VERY VERY BAD! Console commands shouldn't be specified that way. Every parameter should define a label.
     * By Milestone 2 requirements, this is unfortunately not allowed, as every call must be exactly as defined in condition.
     *
     * The calls should've be of this form:
     *
     * WikiXtractor <<command>> <<--param_1>> <<value_param_1>> ...
     *
     *
     * It was probably expected to catch the input directly as arguments of the main function...
     * BUT WHY KEEP IT SIMPLE? KEEP IT FUN!
     *
     * Anyway, the only solution is to adapt the service to the requirements. So we'll have to create for each implementation
     * a validator and a extractor function, that will extract and validate each parameter.
     *
     * The input is a list of string, each element is a space-isolated console argument.
     */

    /**
     * List of string elements that represent the args parameters
     */
    @Parameter(required = true)
    protected List<String> input;

    /**
     * Get name of the command. Needed for registration in JCommander
     *
     * @return Command name
     */
    public abstract String getName();

    /**
     * Validate input. It could also be defined to validate every elements, but it's not very nice, as we also have
     * defined the extractParameters method to map every list elements to needed class variable. That means that we don't
     * want this method to know about position of input variables
     *
     * @param paramNumber Number of awaited parameters
     * @return Command object
     * @throws InvalidCLIInputException
     */
    public CLICommand validateInput(int paramNumber) throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != paramNumber) {
            throw new InvalidCLIInputException(String.format("Number of given parameters doesn't match the \"%s\" command!", getName()));
        }

        return this;
    }

    /**
     * Map every list element of input to a class variable.
     *
     * @return Command object
     */
    public abstract CLICommand extractParameters() throws Exception;

}
