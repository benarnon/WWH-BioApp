package BasicComponent;

import java.util.ArrayList;

/**
 * Created by user on 5/10/15.
 */
public class GeneUnit {
    private String GenomeName;
    private int length;
    private DatabaseIndex[] DbList;



    public GeneUnit(String genomeName, int length, DatabaseIndex[] dbList) {
        GenomeName = genomeName;
        this.length = length;
        DbList = dbList;
    }

    public String getGenomeName() {
        return GenomeName;
    }

    public void setGenomeName(String genomeName) {
        GenomeName = genomeName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DatabaseIndex[] getDbList() {
        return DbList;
    }

    public void setDbList(DatabaseIndex[] dbList) {
        DbList = dbList;
    }
}
