package wikiXtractor.neo4j.service;

import wikiXtractor.model.Entity;
import org.neo4j.ogm.session.Session;

public abstract class GenericService<T> implements Service<T> {

    protected static final int DEPTH_LIST = 0;
    protected static final int DEPTH_ENTITY = 2;
    protected Session session;

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(getEntityType());
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
    public T createOrUpdate(T entity) {
        session.save(entity);
        return find(((Entity) entity).getId());
    }

    public abstract Class<T> getEntityType();
}