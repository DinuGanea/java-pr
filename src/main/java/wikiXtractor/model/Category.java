package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Category extends Entity {

    private String name;

    public Category() {

    }


    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }
}
