package FeatureRelatedComponent;

import BasicComponent.DatabaseIndex;

/**
 * Created by user on 5/10/15.
 */
public class GeneUnit extends Member {
    private int length;
    private DatabaseIndex[] DbList;



    public GeneUnit(String genomeName, int length, DatabaseIndex[] dbList) {
        super(genomeName);
        this.length = length;
        DbList = dbList;
    }

    public String getGenomeName() {
        return getName();
    }

    public void setGenomeName(String genomeName) {
        setName(genomeName);
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
