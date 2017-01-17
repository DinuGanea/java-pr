package wikiXtractor.mapper;

import wikiXtractor.model.City;
import wikiXtractor.model.Person;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapper implementation for city objects
 *
 * @author Ionut Urs
 */
public class CityMapper implements Mappable<City> {

    /**
     * We could've use reflection to map all this properties to method names. By parsing the map, we would invoke
     * each method of the given class with the needed parameter(s).
     *
     * The disadvantage is that we'll lose control over method in string variables, which means, every time a method
     * will be refactored, we'll need to check method invocation again, and eventually, build an hard-coded check
     * in the loop.
     */
    public static final String COUNTRY_KEY = "Staat";
    public static final String FEDERAL_STATE_KEY = "Bundesland";
    public static final String POPULATION_NR_KEY = "Einwohner";
    public static final String FOUNDATION_YEAR_KEY = "Gründung";
    public static final String FOUNDATION_YEAR_ALT_KEY = "foundationYear";

    // Instance of CityMapper
    private static CityMapper instance;

    /**
     * We don't want to use multiple implementation for the same type of object, they are not necessary
     *
     * @return CityMapper instance
     */
    public static CityMapper getInstance() {
        if (instance == null) {
            instance = new CityMapper();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public City map(City city, HashMap<String, String> properties) {

        String country = properties.get(COUNTRY_KEY);
        if (country != null) {
            city.setCountry(properties.get(COUNTRY_KEY));
        } else if (properties.get(FEDERAL_STATE_KEY) != null) {
            city.setCountry("Deutschland");
        }

        String population = properties.get(POPULATION_NR_KEY);
        if (population != null) {
            Pattern p = Pattern.compile("([0-9]+?) \\(");
            Matcher m = p.matcher(population);

            try {
                if (m.find()) {
                    city.setPopulationNr(Integer.parseInt(m.group(1)));
                } else {
                    // Would eventually throw a NumberFormatException
                    city.setPopulationNr(Integer.parseInt(population));
                }
            } catch(NumberFormatException e) {
                // We won't do anything though
            }
        }

        try {
            String foundationYear = properties.get(FOUNDATION_YEAR_KEY);
            if (foundationYear != null) {
                city.setFoundationYear(Integer.parseInt(foundationYear));
            } else if (properties.get(FOUNDATION_YEAR_ALT_KEY) != null) {
                city.setFoundationYear(Integer.parseInt(properties.get(FOUNDATION_YEAR_ALT_KEY)));
            }
        } catch (NumberFormatException e) {
            // We won't do anything though
        }


        return city;
    }
}
