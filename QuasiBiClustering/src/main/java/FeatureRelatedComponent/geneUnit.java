package FeatureRelatedComponent;

import BasicComponent.DatabaseIndex;

/**
 * Created by user on 5/10/15.
 */
public class geneUnit extends member {
    private int length;


    public geneUnit(String genomeName, int length) {
        super(genomeName);
        this.length = length;
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

}