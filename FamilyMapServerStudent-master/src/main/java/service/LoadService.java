package service;

import service.requests.LoadRequest;
import service.results.LoadResult;

/**
 * A service object for the load API.
 */
public class LoadService {

    /**
     * Create a LoadService object.
     */
    public LoadService() {}

    /**
     * Load provided users, persons, and events into the database.
     *
     * @param r a LoadRequest object.
     * @return a LoadResult object.
     */
    public LoadResult load(LoadRequest r) {return null;}
}
