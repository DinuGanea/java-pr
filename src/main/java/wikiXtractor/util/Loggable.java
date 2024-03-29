package wikiXtractor.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Provides logging objects and evt. functionality.
 *
 * @author Dinu Ganea
 */
public interface Loggable {

    /** Logger object. Can be used in all classes that are implementing this interface. Log Channel will be the class name */
    Logger logger = LogManager.getLogger(String.class.getName());

}
