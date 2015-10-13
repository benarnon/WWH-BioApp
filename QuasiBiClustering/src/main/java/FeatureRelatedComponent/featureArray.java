package FeatureRelatedComponent;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class featureArray {
    protected ArrayList<feature> FeaturesArray= new ArrayList<>();


    public void createNewFeature(int index, String featureName) throws Exception {
        feature Feature;
        Feature = new geneUnitFeature();
        FeaturesArray.add(index,Feature);
    }

    public void createNewFeature(String featureName) throws Exception {
        feature Feature;
        Feature = new geneUnitFeature();
        FeaturesArray.add(Feature);
    }

    public void addFeature(int index, feature fe){
        FeaturesArray.add(index,fe);
    }

    public void addFeatureMemeber(String featureName, member featureMemeber) throws Exception {
        int index= getFeatureIndex(featureName);
        if(index == -1){
            throw new Exception("No such feature exist");
        }
        FeaturesArray.get(index).addMember(featureMemeber);

    }

    public int getFeatureIndex(String featureName){
        for (int i = 0; i < FeaturesArray.size(); i++) {
            if(featureName.equals(FeaturesArray.get(i).getFeatureName())){
                return i;
            }
        }
        return -1;
    }

    public feature getFeature(String featureName) {
        return FeaturesArray.get(getFeatureIndex(featureName));
    }

    public feature getFeature(int index){
        return FeaturesArray.get(index);
    }

    public member getMember(String featureName, String memberName){
        return getFeature(featureName).getMember(memberName);
    }

    public member getMember(String featureName, int memberIndex){
        return getFeature(featureName).getMember(memberIndex);
    }

    public member getMember(int featureIndex, int memberIndex){
        return getFeature(featureIndex).getMember(memberIndex);
    }

    public void setFeatureMemberArray(String featureName,ArrayList<member> featureMemebers){
        feature fe =  FeaturesArray.get(getFeatureIndex(featureName));
        fe.setFeatureMemebers(featureMemebers);
    }

    public int getNumOfFeatures(){
        return FeaturesArray.size();
    }

}
