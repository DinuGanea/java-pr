package wikiXtractor.mapper;

import wikiXtractor.model.Entity;

import java.util.HashMap;

/**
 * Interface that defines operations of translation/assignment of raw properties to an entity object
 *
 * @param <T>
 * @author Dinu Ganea
 */
public interface Mappable<T> {

    /**
     * For each property of the map, identify and set assigned the value to the entity
     *
     * @param entity Entity object towards the mapping will be done
     * @param properties Map of properties
     * @return Entity containing all mapped values
     */
    public T map(T entity, HashMap<String, String> properties);

}
