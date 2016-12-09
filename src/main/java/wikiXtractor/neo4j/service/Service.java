package wikiXtractor.neo4j.service;


import java.util.Collection;

public interface Service<T> {

    Collection<T> findAll();

    T find(Long id);

    void delete(Long id);

    void createOrUpdate(T object);
}
