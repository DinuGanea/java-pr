package wikiXtractor.mapper;

import wikiXtractor.model.City;
import wikiXtractor.model.Monument;
import wikiXtractor.model.Person;
import wikiXtractor.util.Loggable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapper implementation for monuments objects
 *
 * @author Ionut Urs
 */
public class MonumentMapper implements Mappable<Monument>, Loggable {

    /**
     * We could've use reflection to map all this properties to method names. By parsing the map, we would invoke
     * each method of the given class with the needed parameter(s).
     *
     * The disadvantage is that we'll lose control over method in string variables, which means, every time a method
     * will be refactored, we'll need to check method invocation again, and eventually, build an hard-coded check
     * in the loop.
     */
    public static final String INAUGURATION_YEAR = "inaugurationYear";
    public static final String SITE = "site";
    public static final String NAME = "name";
    public static final String HONORED_PERSON = "honoredPerson";

    // Instance of the current object
    private static MonumentMapper instance;


    /**
     * We don't want to use multiple implementation for the same type of object, they are not necessary
     *
     * @return MonumentMapper instance
     */
    public static MonumentMapper getInstance() {
        if (instance == null) {
            instance = new MonumentMapper();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Monument map(Monument monument, HashMap<String, String> properties) {

        String inaugurationYear = properties.get(INAUGURATION_YEAR);
        if (inaugurationYear != null) {
            try {
                monument.setInaugurationYear(Integer.parseInt(inaugurationYear));
            } catch (IllegalArgumentException e) {
                logger.warn("Inauguration year is an invalid integer: {}", inaugurationYear);
            }
        }

        String site = properties.get(SITE);
        if (inaugurationYear != null) {
            monument.setSite(site);
        }

        String name = properties.get(NAME);
        if (name != null) {
            monument.setSite(name);
        }

        String honoredPerson = properties.get(HONORED_PERSON);
        if (name != null) {
            monument.setSite(honoredPerson);
        }

        return monument;
    }

}
