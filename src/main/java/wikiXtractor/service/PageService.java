package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.Page;
import wikiXtractor.neo4j.service.GenericService;


public class PageService extends GenericService<Page> {

    public PageService(Session session) {
        this.session = session;
    }

    @Override
    public Class<Page> getEntityType() {
        return Page.class;
    }
}
