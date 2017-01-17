package wikiXtractor.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entity for a Monument object
 *
 * @author Delara Nasri
 */
@NodeEntity
public class Monument extends ContentEntity {

    // Self explanatory properties
    private String name;
    private String site;
    private int inaugurationYear;
    private Person honoredPerson;

    /**
     * Pattern used for regular expression to identify monuments
     */
    protected static Pattern pattern = Pattern.compile("Denkmal", Pattern.CASE_INSENSITIVE);


    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public Monument() {

    }

    /**
     * @return Monuments' title
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Monument's title
     * @return Monument instance
     */
    public Monument setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return Place where the monument is localised
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site Place where the monument is localised
     * @return Monument instance
     */
    public Monument setSite(String site) {
        this.site = site;
        return this;
    }

    /**
     * @return Monument's inauguration year
     */
    public int getInaugurationYear() {
        return inaugurationYear;
    }

    /**
     * @param inaugurationYear Monument's inauguration year
     * @return Monument instance
     */
    public Monument setInaugurationYear(int inaugurationYear) {
        this.inaugurationYear = inaugurationYear;
        return this;
    }

    /**
     * @return Person if whose honor, the monument was built
     */
    public Person getHonoredPerson() {
        return honoredPerson;
    }

    /**
     * @param honoredPerson Person if whose honor, the monument was built
     * @return Monument instance
     */
    public Monument setHonoredPerson(Person honoredPerson) {
        this.honoredPerson = honoredPerson;
        return this;
    }

    /**
     * Identify an article object as a monument
     *
     * @param category Category towards the check will be made
     * @return True if the category identifies a monument, false otherwise
     */
    public static boolean matchesCategory(String category) {
        Matcher matcher = pattern.matcher(category);
        return matcher.find();
    }

    /**
     * Bunch all the info about a monument, so we can easily display it
     *
     * @return Monument's info
     */
    public String toString() {

        StringBuilder info = new StringBuilder();

        info.append("Name: ").append(name).append("\n");
        info.append("Site: ").append(site).append("\n");
        info.append("Inauguration year: ").append(inaugurationYear).append("\n");
        info.append("Honored Person: ").append(honoredPerson).append("\n");

        return info.toString();
    }
}
