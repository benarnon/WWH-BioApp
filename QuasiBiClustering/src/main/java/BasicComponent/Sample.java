package BasicComponent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 5/10/15.
 */
public class Sample {
    private String sampleName;
    private Date date;
    private String location;
    private String[] symptoms;
    private ArrayList<GeneUnit> mappedGeneUnitsList = new ArrayList<>();

    public Sample(String sampleName, Date date, String location, String[] symptoms) {
        this.sampleName = sampleName;
        this.date = date;
        this.location = location;
        this.symptoms = symptoms;
    }

    public Sample(String sampleName){
        this.sampleName = sampleName;
        this.date = null;
        this.location = null;
        this.symptoms = null;
    }

    public ArrayList<GeneUnit> getMappedGeneUnitsList() {
        return mappedGeneUnitsList;
    }

    public void setMappedGeneUnitsList(ArrayList<GeneUnit> mappedGeneUnitsList) {
        this.mappedGeneUnitsList = mappedGeneUnitsList;
    }

    public void addGenome(GeneUnit geneUnit){
        mappedGeneUnitsList.add(geneUnit);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String[] symptoms) {
        this.symptoms = symptoms;
    }
}
