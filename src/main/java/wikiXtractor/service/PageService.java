package wikiXtractor.service;

import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;

import wikiXtractor.model.*;
import wikiXtractor.neo4j.service.GenericService;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.util.Utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class PageService<T extends Page> extends GenericService<T> {

    private Class<T> type;

    public PageService(Session session, Class<T> type) throws Exception {
        this.session = session;
        this.type = type;

        if (!Page.class.isAssignableFrom(type)) {
            throw new Exception(String.format("%s must be a Page implementation!", type.getName()));
        }

    }

    public Class<T> getEntityType() {
        return type;
    }

    public T find(int namespaceID, String title) {
        Filter filter = new Filter(Entity.CUSTOM_ID_PROP_NAME, Utility.getMD5(namespaceID + "" + title));

        Filters filters = new Filters();
        filters.add(filter);

        Collection<T> pages = session.loadAll(getEntityType(), filters);

        if (pages.size() > 0) {
            return pages.iterator().next();
        } else {
            return null;
        }
    }

    public Collection<T> find(int namespaceID) {
        Filter filter = new Filter(Page.NS_EL_NAME, namespaceID);

        return session.loadAll(getEntityType(), filter, DEPTH_LIST);
    }
}
