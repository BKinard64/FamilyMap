package jsondata;

public class Location {
    String latitude;
    String longitude;
    String city;
    String country;

    public Location(String latitude, String longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
