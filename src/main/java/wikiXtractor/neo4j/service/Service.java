package wikiXtractor.neo4j.service;


import java.util.Collection;

/**
 * DB operations service. Defines default supported operations to the database
 *
 * @param <T>
*  @author Dinu Ganea
 */
public interface Service<T> {

    /**
     * Parse the entire graph and return all objects of the given type.
     *
     * Use this method very carefully. With great power comes great responsibility!!!
     *
     * It could cause performance problems as we need to extract all objects and create instances for each one.
     *
     * @return Collection of objects
     */
    Collection<T> findAll();

    /**
     * Find the object by given Graph ID.
     *
     * @param id Object graph ID
     * @return Object
     */
    T find(Long id);

    /**
     * Delete object from database with given ID
     *
     * @param id Object ID
     */
    void delete(Long id);

    /**
     * Insert or update object.
     *
     * Update operation is made ONLY by object's graph ID.
     *
     * You want to update an object by namespaceID and title? IT WON'T HAPPEN
     * You want to update an object by it's custom ID? IT WON'T HAPPEN
     * You want to update an object by it's instance? Guess what?!!
     *
     * @param object Object to be updated
     */
    void upsert(T object);
}
