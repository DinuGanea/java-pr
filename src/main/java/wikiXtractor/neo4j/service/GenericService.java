package wikiXtractor.neo4j.service;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.event.Event;
import wikiXtractor.model.Entity;


import java.util.Collection;

/**
 * Another abstraction layer for the DB operations services.
 * This generic class defines some functionality that could be used for all object types
 *
 * @param <T> service class type
 * @author Dinu Ganea
 */
public abstract class GenericService<T> implements Service<T> {

    // Horizon of the DB operations.
    // Defines how wide will the DB operation influence objects and/or instantiate them
    // The bigger depth, the bigger runtime
    // Use mainly for mass search operation
    protected static final int DEPTH_LIST = 0;
    // Use for single entity operation
    protected static final int DEPTH_ENTITY = 1;

    // DB session object
    protected Session session;

    @Override
    /**
     * {@inheritDoc}
     */
    public Collection<T> findAll() {
        return session.loadAll(getEntityType(), DEPTH_LIST);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public T find(Long id) {
        return session.load(getEntityType(), id);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void delete(Long id) {
        session.delete(session.load(getEntityType(), id));
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void upsert(T entity) {
        session.save(entity, DEPTH_ENTITY);
    }


    /**
     * Used for the abstraction layer, as the generic service doesn't need to know about the object type that's being used
     *
     * @return Class type
     */
    public abstract Class<T> getEntityType();
}