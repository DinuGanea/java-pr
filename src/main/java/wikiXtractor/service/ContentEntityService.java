package wikiXtractor.service;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import wikiXtractor.model.*;
import wikiXtractor.neo4j.service.GenericService;

import java.util.Collections;
import java.util.HashMap;

/**
 * Generic DB service ONLY for content entity objects. It can accept different instances type tough, to be more specific.
 * If unsure what type of page object is needed, give the ContentEntity.class as type.
 *
 *
 * @param <T> - type of the class the service works with
 * @author Ionut Urs
 */
public class ContentEntityService<T extends ContentEntity> extends GenericService<T> {

    // type of given class
    protected Class<T> type;


    /**
     * Create DB service for the given type
     *
     * @param session Neo4j DB session
     * @param type Class Type
     * @throws Exception
     */
    public ContentEntityService(Session session, Class<T> type) throws Exception {
        this.session = session;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getEntityType() {
        return type;
    }


    /**
     * <p>
     * Extract all content entities objects from DB. The type is specified at class instantiation
     *
     * @return HashMap whose key is content entity name and value - the object itself
     */
    public HashMap<String, T> findAllContentEntities() {

        // Build query
        String query = String.format("MATCH (e:%s)<-[:%s]-(a:%s) return e, a",
                type.getSimpleName(),
                Reference.TYPE,
                Article.class.getSimpleName()
        );

        // Execute and extract results
        Result result = session.query(query, Collections.emptyMap());

        HashMap<String, T> contentEntities = new HashMap<>();

        // Hydrate all extracted objects objects to HashMap
        result.forEach(o -> {
            T contentEntity = (T) o.get("e");
            contentEntity.setSourcePage((Page) o.get("a"));
            contentEntities.put(contentEntity.getRawTitle(), contentEntity);
        });

        return contentEntities;
    }


    /**
     * Get all referrers of the given entity type object
     *
     * @param contentEntity ContentEntity instance
     * @return HashMap whose key is referrer content entity name and value - the object itself
     */
    public HashMap<String, ContentEntity> getReferrers(ContentEntity contentEntity) {
        // Build query
        String query = String.format("MATCH (referrer:ContentEntity)-[:`%s`]->(entity:ContentEntity) WHERE ID(entity) = %s return referrer",
                Reference.TYPE,
                contentEntity.getId()
        );

        // Execute and extract results
        Result result = session.query(query, Collections.emptyMap());

        HashMap<String, ContentEntity> contentEntities = new HashMap<>();

        // Hydrate all extracted objects objects to HashMap
        result.forEach(o -> {
            ContentEntity referrer = (ContentEntity) o.get("referrer");
            contentEntities.put(referrer.getRawTitle(), referrer);
        });

        return contentEntities;
    }


    /**
     * Get all referrals of the given entity type object
     *
     * @param contentEntity ContentEntity instance
     * @return HashMap whose key is referral content entity name and value - the object itself
     */
    public HashMap<String, ContentEntity> getReferrals(ContentEntity contentEntity) {
        // Build query
        String query = String.format("MATCH (referral:ContentEntity)<-[:`%s`]-(entity:ContentEntity) WHERE ID(entity) = %s return referral",
                Reference.TYPE,
                contentEntity.getId()
        );

        // Execute and extract results
        Result result = session.query(query, Collections.emptyMap());

        HashMap<String, ContentEntity> contentEntities = new HashMap<>();

        // Hydrate all extracted objects objects to HashMap
        result.forEach(o -> {
            ContentEntity referral = (ContentEntity) o.get("referral");
            contentEntities.put(referral.getRawTitle(), referral);
        });

        return contentEntities;
    }
}
