package wikiXtractor.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Entity for a City object
 *
 * @author Delara Nasri
 */
@NodeEntity
public class City extends ContentEntity {

    // Self explanatory properties
    private String name;
    private String country;
    private int populationNr;
    private int foundationYear;

    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public City() {

    }

    /**
     * @return City's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name City's name
     * @return City instance
     */
    public City setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return City's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country City's country
     * @return City instance
     */
    public City setCountry(String country) {
        this.country = country;
        return this;
    }

    /**
     * @return City's population number
     */
    public int getPopulationNr() {
        return populationNr;
    }

    /**
     * @param populationNr City's population number
     * @return city instance
     */
    public City setPopulationNr(int populationNr) {
        this.populationNr = populationNr;
        return this;
    }

    /**
     * @return City's foundation year
     */
    public int getFoundationYear() {
        return foundationYear;
    }

    /**
     * @param foundationYear City's foundation year
     * @return City instacne
     */
    public City setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
        return this;
    }

    /**
     * Identify an article object as a city object
     *
     * @param category Category towards the check will be made
     * @return True if the category identifies a city, false otherwise
     */
    public static boolean matchesCategory(String category) {

        String[] keywords = new String[] {
                "Ort", "Gemeinde",
                "Ortsteil", "stadt"
        };

        for (String keyword : keywords) {
            if (Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(category).find()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Bunch all the info about a monument, so we can easily display it
     *
     * @return City's info
     */
    public String toString() {

        StringBuilder info = new StringBuilder();

        info.append("Name: ").append(name).append("\n");
        info.append("Country: ").append(country).append("\n");
        info.append("Population Number: ").append(populationNr).append("\n");
        info.append("Foundation year: ").append(foundationYear).append("\n");

        return info.toString();
    }
}
