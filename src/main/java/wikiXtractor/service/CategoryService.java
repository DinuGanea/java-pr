package wikiXtractor.service;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import wikiXtractor.model.*;

import java.util.*;

/**
 * Specific implementation of the page service object.
 *
 * @author Ionut Urs
 */
public class CategoryService extends PageService<Category> {

    /**
     * {@inheritDoc}
     */
    public CategoryService(Session session) throws Exception {
        super(session, Category.class);
    }

    /**
     * Find category by name. We can do that, as we know that it's namespace is defined
     *
     * @param name Category name
     * @return Category object
     */
    public Category find(String name) {
        return super.find(Category.NAMESPACE_ID, name);
    }


    /**
     * TODO rewrite embedded ogm driver
     * <p>
     * Create parenthood relationship between given category object and it's subcategories
     * <p>
     * Bad implementation, but without this, performance is really shitty. We could've use the upsert method
     * from the service, but the embedded driver is reeeeaaaly slow. We'll have to rewrite it for more performance.
     *
     * @param category Category object
     */
    public void resolveParenthood(Category category) {

        StringBuilder query = new StringBuilder();
        StringBuilder relationships = new StringBuilder();

        // Match the parent node
        query.append(String.format("MATCH (startNode) WHERE ID(startNode) = %s\n", category.getId()));

        int i = 0;
        for (Category subcategory : category.getSubcategories()) {
            // Match all subcategories
            query.append(String.format("MATCH (endNode_%d) WHERE ID(endNode_%d) = %s\n", i, i, subcategory.getId()));
            // Create the actual relationship between the parent and the current child
            relationships.append(String.format("CREATE (startNode)-[:`%s`]->(endNode_%d)\n", Parenthood.TYPE, i));
            i++;
        }

        // Add CREATE queries to the match queries
        query.append(relationships.toString());

        // Execute!
        session.query(query.toString(), Collections.emptyMap());
    }

    /**
     * TODO rewrite embedded ogm driver
     *
     * Same problem as for resolveParenthood method. Pure CQL instead of object mapping
     *
     * Extract all categories from the DB.
     *
     * @return HashMap whose key is the category name and value is the actual object
     */
    public HashMap<String, Category> findAllCats() {

        // Build the query
        String query = String.format("MATCH (cat:Category{%s:%d}) return cat",
                Page.NS_EL_NAME,
                Category.NAMESPACE_ID
        );

        // Execute and get results
        Result result = session.query(query, Collections.emptyMap());

        HashMap<String, Category> categories = new HashMap<>();

        // Hydrate results to the HashMap object
        result.forEach(o -> {
            Category cat = (Category) o.get("cat");
            categories.put(cat.getPageTitle(), cat);
        });

        return categories;
    }


    /**
     * TODO rewrite embedded ogm driver
     *
     * Get parent categories of a specific category
     *
     * @param category Category object
     * @return LinkedHashMap, whose key is the category name and value is the object itself
     */
    public LinkedHashMap<String, Category> getParentCategories(Category category) {

        LinkedHashMap<String, Category> parents = new LinkedHashMap<>();

        String query = String.format("MATCH (c:Category)-[:`%s`*]->(cat:Category) WHERE ID(cat) = %s return c",
                Parenthood.TYPE,
                category.getId()
        );

        Result result = session.query(query, Collections.emptyMap());


        // Hydrate results to the LinkedHashMap object
        result.forEach(o -> {
            Category cat = (Category) o.get("c");
            parents.put(cat.getPageTitle(), cat);
        });

        return parents;
    }

    /**
     * TODO rewrite embedded ogm driver
     *
     * Get children for the given category of depth 1
     *
     * @param category Category object
     * @return LinkedHashMap, whose key is the category name and value is the object itself
     */
    public LinkedHashMap<String, Category> getDirectChildCategories(Category category) {

        LinkedHashMap<String, Category> children = new LinkedHashMap<>();

        String query = String.format("MATCH (c:Category)-[:`%s`*]->(cat:Category) WHERE ID(c) = %s return cat",
                Parenthood.TYPE,
                category.getId()
        );

        Result result = session.query(query, Collections.emptyMap());


        // Hydrate results to the LinkedHashMap object
        result.forEach(o -> {
            Category cat = (Category) o.get("cat");
            children.put(cat.getPageTitle(), cat);
        });

        return children;
    }
}
