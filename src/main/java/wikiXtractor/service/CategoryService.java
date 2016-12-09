package wikiXtractor.service;

import org.neo4j.ogm.model.Result;
import wikiXtractor.model.Category;
import wikiXtractor.model.Parenthood;
import wikiXtractor.neo4j.session.Session;

import java.util.Collections;
import java.util.HashMap;


public class CategoryService extends PageService<Category> {

    public CategoryService(Session session) throws Exception {
        super(session, Category.class);
    }

    public Category find(String name) {
        return super.find(Category.NAMESPACE_ID, name);
    }

    // Bad implementation, but without this, performance is really shitty
    public Result addParenthood(Long customIDParent, Long customIDChild) {
        String query = String.format("" +
                "MATCH (startNode) WHERE ID(startNode) = %s MATCH (endNode) WHERE ID(endNode) = %s CREATE (startNode)-[rel:`PARENT_OF`]->(endNode)"
                        /*"MATCH (p:Category{id:%d}), (c:Category{id:%d}) " +
                        "CREATE (p)-[:%s]->(c)"*/
                , customIDParent
                , customIDChild
                , Parenthood.TYPE);


        return session.query(query, Collections.emptyMap());
    }

    public HashMap<String, Category> findAllCats() {
        String query = String.format("" +
                        "MATCH (p:Category{%s:%d}) return p",
                "namespaceID",
                14
        );


        Result result = session.query(query, Collections.emptyMap());


        HashMap<String, Category> categories = new HashMap<>();

        result.forEach(o -> {
            Category p = (Category) o.get("p");
            categories.put(p.getPageTitle(), p);
        });


        //System.out.println(String.format("%d categories found", i));
        //result.forEach(System.out::println);


        return categories;
    }
}
