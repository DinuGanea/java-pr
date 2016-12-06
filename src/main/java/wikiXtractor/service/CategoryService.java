package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.service.GenericService;


public class CategoryService extends PageService<Category> {

    public CategoryService(Session session) throws Exception {
        super(session, Category.class);
    }
}
