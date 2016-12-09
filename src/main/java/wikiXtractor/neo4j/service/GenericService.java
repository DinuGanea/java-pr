package wikiXtractor.neo4j.service;

import org.neo4j.ogm.session.event.Event;
import wikiXtractor.model.Entity;
import wikiXtractor.neo4j.session.Session;


import java.util.Collection;

public abstract class GenericService<T> implements Service<T> {

    protected static final int DEPTH_LIST = 0;
    protected static final int DEPTH_ENTITY = 1;
    protected Session session;

    @Override
    public Collection<T> findAll() {
        return session.loadAll(getEntityType(), 0);
    }

    @Override
    public T find(Long id) {
        return session.load(getEntityType(), id);
    }

    @Override
    public void delete(Long id) {
        session.delete(session.load(getEntityType(), id));
    }

    @Override
    public void createOrUpdate(T entity) {
        session.save(entity, 2);
    }

    public abstract Class<T> getEntityType();
}