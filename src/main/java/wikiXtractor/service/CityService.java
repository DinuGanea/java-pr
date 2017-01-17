package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.City;
import wikiXtractor.neo4j.service.GenericService;

/**
 * Specific implementation of the content entity service
 *
 * @author Ionut Urs
 */
public class CityService extends ContentEntityService<City> {

    /**
     * {@inheritDoc}
     */
    public CityService(Session session) throws Exception {
        super(session, City.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<City> getEntityType() {
        return City.class;
    }

}
