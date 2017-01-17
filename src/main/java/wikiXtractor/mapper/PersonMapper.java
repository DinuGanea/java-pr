package wikiXtractor.mapper;


import wikiXtractor.model.Person;

import java.util.HashMap;

/**
 * Mapper implementation for person objects
 *
 * @author Ionut Urs
 */
public class PersonMapper implements Mappable<Person> {

    /**
     * We could've use reflection to map all this properties to method names. By parsing the map, we would invoke
     * each method of the given class with the needed parameter(s).
     *
     * The disadvantage is that we'll lose control over method in string variables, which means, every time a method
     * will be refactored, we'll need to check method invocation again, and eventually, build an hard-coded check
     * in the loop.
     */
    public static final String LAST_NAME_KEY = "NAME";
    public static final String FULL_NAME_KEY = "ALTERNATIVNAMEN";
    public static final String BIRTHDAY_KEY = "GEBURTSDATUM";
    public static final String BIRTHPLACE_KEY = "GEBURTSORT";
    public static final String OBIT_KEY = "STERBEDATUM";


    // Instance of the current object
    private static PersonMapper instance;

    /**
     * We don't want to use multiple implementation for the same type of object, they are not necessary
     *
     * @return PersonMapper instance
     */
    public static PersonMapper getInstance() {
        if (instance == null) {
            instance = new PersonMapper();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person map(Person person, HashMap<String, String> properties) {

        String lastName = properties.get(LAST_NAME_KEY);
        if (lastName != null) {
            person.setLastname(properties.get(LAST_NAME_KEY));
        }

        String fullName = properties.get(FULL_NAME_KEY);
        if (fullName != null) {
            if (lastName != null) {
                person.setFirstname(fullName.replace(lastName, "").trim());
            } else {
                person.setFirstname(fullName);
            }

            person.setBirthname(fullName);
        }

        String birthday = properties.get(BIRTHDAY_KEY);
        if (birthday != null) {
            person.setBirthday(birthday);
        }

        String birthplace = properties.get(BIRTHPLACE_KEY);
        if (birthplace != null) {
            person.setBirthplace(birthday);
        }

        String obit = properties.get(OBIT_KEY);
        if (obit != null) {
            person.setObit(obit);
        }

        return person;
    }

}
