package service;

import service.requests.PersonRequest;
import service.results.PersonResult;

/**
 * A service object for the person/[personID] API.
 */
public class PersonService {

    /**
     * Create a PersonService object.
     */
    public PersonService() {}

    /**
     * Return a Person object.
     *
     * @param r a PersonRequest object.
     * @return a PersonResult object.
     */
    public PersonResult person(PersonRequest r) {return null;}
}
