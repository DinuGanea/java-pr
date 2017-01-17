package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.ContentEntity;
import wikiXtractor.model.Monument;
import wikiXtractor.neo4j.service.GenericService;

/**
 * Specific implementation of the content entity service
 *
 * @author Ionut Urs
 */
public class MonumentService extends ContentEntityService<Monument> {

    /**
     * {@inheritDoc}
     */
    public MonumentService(Session session) throws Exception {
        super(session, Monument.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Monument> getEntityType() {
        return Monument.class;
    }
}
