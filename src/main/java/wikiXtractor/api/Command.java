package wikiXtractor.api;


/**
 * Defines methods that could build an abstraction layer for all command implementations
 *
 * @param <T> type return type of the command class
 * @author Dinu Ganea
 */
public interface Command<T> {

    /**
     * Execute the set of instruction defined by the specific command
     *
     * @return Result type defined by the class implementation
     * @throws Exception
     */
    public T execute() throws Exception;

}
