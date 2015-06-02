package BasicComponent;

import FeatureRelatedComponent.*;


import java.util.Date;

/**
 * Created by user on 5/10/15.
 */
public class Sample {
    private String sampleName;
    private Date date;
    private String location;
    private int numOfFeatures = 0;

    private FeatureArray featureArray = new FeatureArray();

    public Sample(String sampleName, Date date, String location) {
        this.sampleName = sampleName;
        this.date = date;
        this.location = location;
    }

    public Sample(String sampleName){
        this.sampleName = sampleName;
        this.date = null;
        this.location = null;
    }

    public void createNewFeature(String featureName, int index) throws Exception {
        featureArray.createNewFeature(index, featureName);
        numOfFeatures++;
    }

    public void createNewFeature(String featureName) throws Exception {
        featureArray.createNewFeature(featureName);
        numOfFeatures++;
    }
    public void addFeatrue(int index, feature fe){
        featureArray.addFeature(index,fe);
    }

    public feature getFeature(String featureName){
        return featureArray.getFeature(featureName);
    }

    public Member getMember(String featureName,int memberIndex){
        return featureArray.getMember(featureName,memberIndex);
    }

    public Member getMember(String featureName,String memberName){
        return featureArray.getMember(featureName,memberName);
    }

    public void setMember(String featureName,Member member) throws Exception {
        featureArray.addFeatureMemeber(featureName,member);
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



}
