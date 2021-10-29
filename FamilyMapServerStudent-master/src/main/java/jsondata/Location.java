package jsondata;

public class Location {
    private final Float latitude;
    private final Float longitude;
    private final String city;
    private final String country;

    public Location(Float latitude, Float longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
