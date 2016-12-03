package wikiXtractor.service;

import wikiXtractor.model.Category;
import wikiXtractor.neo4j.service.GenericService;


public class CategoryService extends GenericService<Category> {

    @Override
    public Class<Category> getEntityType() {
        return Category.class;
    }
}
