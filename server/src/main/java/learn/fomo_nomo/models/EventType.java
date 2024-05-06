package learn.fomo_nomo.models;

public enum EventType {
    SOCIAL(1,"social"),
    WORK(2,"work"),
    PERSONAL(3,"personal"),
    APPOINTMENT(4,"appointment");

    private final int value;
    private final String name;

    EventType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


}
