package wikiXtractor.service;


import org.neo4j.ogm.session.Session;
import wikiXtractor.model.Person;
import wikiXtractor.neo4j.service.GenericService;

/**
 * Specific implementation of the content entity service
 *
 * @author Ionut Urs
 */
public class PersonService extends ContentEntityService<Person> {

    /**
     * {@inheritDoc}
     */
    public PersonService(Session session) throws Exception {
        super(session, Person.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Person> getEntityType() {
        return Person.class;
    }
}
