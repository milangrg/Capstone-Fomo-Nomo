package learn.fomo_nomo.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId && Objects.equals(address, location.address) && Objects.equals(state, location.state) && Objects.equals(city, location.city) && Objects.equals(postal, location.postal) && Objects.equals(locationName, location.locationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, address, state, city, postal, locationName);
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", postal='" + postal + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}

