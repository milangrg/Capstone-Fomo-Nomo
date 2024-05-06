package learn.fomo_nomo.models;

public class Location {
    private int locationId;
    private String address;
    private String state;
    private String city;
    private String postal;
    private String locationName;

    public Location() {
    }

    public Location(int locationId, String address, String state, String city, String postal, String locationName) {
        this.locationId = locationId;
        this.address = address;
        this.state = state;
        this.city = city;
        this.postal = postal;
        this.locationName = locationName;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

