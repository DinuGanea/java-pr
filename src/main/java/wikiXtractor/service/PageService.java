package wikiXtractor.service;

import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.*;
import wikiXtractor.neo4j.service.GenericService;
import wikiXtractor.util.Utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic DB service ONLY for page objects. It can accept different instances type tough, to be more specific
 * If unsure what type of page object is needed, give the Page.class as type.
 *
 *
 * @param <T> - type of the class the service works with
 * @author Dinu Ganea
 */
public class PageService<T extends Page> extends GenericService<T> {

    // type of given class
    private Class<T> type;

    /**
     * Create a DB service for the given class type.
     *
     * @param session Neo4j DB session
     * @param type Class type
     * @throws Exception
     */
    public PageService(Session session, Class<T> type) throws Exception {
        this.session = session;
        this.type = type;

        if (!Page.class.isAssignableFrom(type)) {
            throw new Exception(String.format("%s must be a Page implementation!", type.getName()));
        }

    }


    /**
     * Find the page object with the given namespaceID and title
     *
     * @param namespaceID Page namespace
     * @param title Page title
     * @return A single object that matches the properties
     */
    public T find(int namespaceID, String title) {

        // Create a filter for the custom id of the entity. We won't search for both properties, so we need a hash for this
        Filter filter = new Filter(Entity.CUSTOM_ID_PROP_NAME, Utility.customHash(namespaceID, title));

        // Add filter to the set. It's a bad practice to use a single property filter. It will be deprecated
        Filters filters = new Filters();
        filters.add(filter);

        // Load all page objects that are matching the filters
        Collection<T> pages = session.loadAll(getEntityType(), filters);

        if (pages.size() > 0) {
            // It's for sure that we have only one object for the give properties, so return it
            return pages.iterator().next();
        } else {
            // No matches!
            return null;
        }
    }

    /**
     * Find all objects that are matching the namespaceID
     *
     * @param namespaceID Page object namespace ID
     * @return Collection of page objects
     */
    public Collection<T> find(int namespaceID) {
        // Create filter for the namespace property
        Filter filter = new Filter(Page.NS_EL_NAME, namespaceID);

        // Bad practice to filter only by one object
        Filters filters = new Filters();
        filters.add(filter);

        return session.loadAll(getEntityType(), filter, DEPTH_LIST);
    }


    /**
     * {@inheritDoc}
     */
    public Class<T> getEntityType() {
        return type;
    }
}
