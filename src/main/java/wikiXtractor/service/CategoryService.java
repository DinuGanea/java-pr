package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.service.GenericService;


public class CategoryService extends GenericService<Category> {

    public CategoryService(Session session) {
        this.session = session;
    }

    @Override
    public Class<Category> getEntityType() {
        return Category.class;
    }
}
