package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Categorisation.TYPE)
public class Categorisation extends Entity {

    public static final String TYPE = "CATEGORY_OF";

    public Categorisation() {

    }

    @StartNode
    private Page page;

    @EndNode
    private Category category;


    public Categorisation setStartNode(Page p) {
        this.page = p;
        return this;
    }

    public Categorisation setEndNode(Category c) {
        this.category = c;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public Category getCategory() {
        return category;
    }


}
