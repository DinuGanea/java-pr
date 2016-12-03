package wikiXtractor.service;

import wikiXtractor.model.Page;
import wikiXtractor.neo4j.service.GenericService;


public class PageService extends GenericService<Page> {

    @Override
    public Class<Page> getEntityType() {
        return Page.class;
    }
}
