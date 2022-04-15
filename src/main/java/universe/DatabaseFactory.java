package universe;

public class DatabaseFactory {

    private static Database database;

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }
}
