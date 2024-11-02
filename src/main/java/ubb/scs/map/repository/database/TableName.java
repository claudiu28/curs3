package ubb.scs.map.repository.database;

public enum TableName {
    UTILIZATORI("UTILIZATORI"),
    PRIETENII("PRIETENII");

    private final String name;

    TableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
