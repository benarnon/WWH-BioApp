package BasicComponent;

/**
 * Created by user on 5/10/15.
 */
public class DatabaseIndex {
    String DB_name;
    String index;

    public DatabaseIndex(String DB_name, String index) {
        this.DB_name = DB_name;
        this.index = index;
    }

    public String getDB_name() {
        return DB_name;
    }

    public void setDB_name(String DB_name) {
        this.DB_name = DB_name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
