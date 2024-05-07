package learn.fomo_nomo.models;

public enum Status {
    ACCEPTED(1,"accepted"),
    DECLINED(2,"declined"),
    PENDING(3,"pending");

    private final int value;
    private final String name;

    Status(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Status findByName(String name) {
        for (Status status : Status.values()) {
            if (status.getName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        String message = String.format("No status with name: %s.", name);
        throw new RuntimeException(message);
    }
}
