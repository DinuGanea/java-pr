package wikiXtractor.api.cli;


import com.beust.jcommander.Parameter;
import wikiXtractor.api.Command;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.api.cli.exceptions.InvalidCLIParameterException;
import wikiXtractor.util.Loggable;

import java.util.List;

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

    @Parameter(required = true)
    protected List<String> input;

    public abstract String getName();

    public abstract CLICommand validateInput() throws InvalidCLIInputException, InvalidCLIParameterException;
    public abstract CLICommand extractParameters();
}
