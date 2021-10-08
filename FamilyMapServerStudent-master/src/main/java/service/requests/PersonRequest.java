package service.requests;

public class PersonRequest {
    private String personID;

    public PersonRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }
}
