package FeatureRelatedComponent;

import Mains.EnumParams;
import com.sun.org.apache.xalan.internal.utils.FeatureManager;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class FeatureArray {
    protected ArrayList<feature> FeaturesArray= new ArrayList<>();


    public void createNewFeature(int index, String featureName) throws Exception {
        feature Feature;
        if (featureName.equals("Symptoms")) {
            Feature = new SymptomsFeature();
        }
        else if(featureName.equals("GeneUnit")){
            Feature = new GeneUnitFeature();
        }
        else{
            throw new Exception("Not a valid name for feature");
        }
        FeaturesArray.add(index,Feature);
    }

    public void createNewFeature(String featureName) throws Exception {
        feature Feature;
        if (featureName.equals(EnumParams.SymptomsFeatureName)) {
            Feature = new SymptomsFeature();
        }
        else if(featureName.equals(EnumParams.GenesFeatureName)){
            Feature = new GeneUnitFeature();
        }
        else{
            throw new Exception("Not a valid name for feature");
        }
        FeaturesArray.add(Feature);
    }

    public void addFeature(int index, feature fe){
        FeaturesArray.add(index,fe);
    }

    public void addFeatureMemeber(String featureName, Member featureMemeber) throws Exception {
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

    public Member getMember(String featureName, String memberName){
        return getFeature(featureName).getMember(memberName);
    }

    public Member getMember(String featureName, int memberIndex){
        return getFeature(featureName).getMember(memberIndex);
    }

    public Member getMember(int featureIndex, int memberIndex){
        return getFeature(featureIndex).getMember(memberIndex);
    }

    public void setFeatureMemberArray(String featureName,ArrayList<Member> featureMemebers){
        feature fe =  FeaturesArray.get(getFeatureIndex(featureName));
        fe.setFeatureMemebers(featureMemebers);
    }

    public int getNumOfFeatures(){
        return FeaturesArray.size();
    }

}
