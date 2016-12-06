package wikiXtractor.model;


public abstract class Entity {

    public static final String CUSTOM_ID_PROP_NAME = "customID";

    private Long id;


    protected int customID;


    public int getCustomID() {
        return customID;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || id == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }
}
