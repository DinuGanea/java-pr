package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Entity for Person object
 *
 * @author Delara Nasri
 */
@NodeEntity
public class Person extends ContentEntity {

    // Self-explanatory properties
    private String firstname = "";
    private String lastname = "";
    private String birthname = "";
    private String birthday = "";
    private String obit = "";
    private String birthplace = "";
    private String lastDomicile = "";


    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public Person() {
    }

    /**
     * @return Person's firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname Person's firstname
     * @return Person instance
     */
    public Person setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * @return Person's lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname Person's lastname
     * @return Person instance
     */
    public Person setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    /**
     * @return Person's birthname
     */
    public String getBirthname() {
        return birthname;
    }

    /**
     * @param birthname Person's birthname
     * @return Person instance
     */
    public Person setBirthname(String birthname) {
        this.birthname = birthname;
        return this;
    }

    /**
     * @return Person's birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday Person's birthday
     * @return Person instance
     */
    public Person setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    /**
     * @return Record of a person's death
     */
    public String getObit() {
        return obit;
    }

    /**
     * @param obit Record of a person's death
     * @return Person instance
     */
    public Person setObit(String obit) {
        this.obit = obit;
        return this;
    }

    /**
     * @return Person's birthplace
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * @param birthplace Person's birthplace
     * @return Person instance
     */
    public Person setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    /**
     * @return Person's last known domicile
     */
    public String getLastDomicile() {
        return lastDomicile;
    }

    /**
     * @param lastDomicile Person's last known domicile
     * @return Person instance
     */
    public Person setLastDomicile(String lastDomicile) {
        this.lastDomicile = lastDomicile;
        return this;
    }

    /**
     * Defines keywords and rules that identify an article object as a person
     *
     * @param category Category name (towards we'll make the check)
     * @return True if category identifies a person, false otherwise
     */
    public static boolean matchesCategory(String category) {
        return category.contains("Person") || category.contains("Mann") || category.contains("Frau");
    }


    /**
     * Bunch all the info about a person, so we can easily display it
     *
     * @return Person's info
     */
    public String toString() {

        StringBuilder info = new StringBuilder();

        info.append("Firstname: ").append(firstname).append("\n");
        info.append("Lastname: ").append(lastname).append("\n");
        info.append("Birthname: ").append(birthname).append("\n");
        info.append("Birthday: ").append(birthday).append("\n");
        info.append("Obit: ").append(obit).append("\n");
        info.append("Birthplace: ").append(birthplace).append("\n");
        info.append("Last domicile: ").append(lastDomicile).append("\n");


        return info.toString();
    }
}
