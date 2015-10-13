package FeatureRelatedComponent;

import java.util.ArrayList;

/**
 * Created by user on 5/31/15.
 */
public abstract class feature {
    protected ArrayList<member> FeatureMemebers = new ArrayList<member>();
    protected  String featureName;

    public feature(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureNAme) {
        this.featureName = featureNAme;
    }

    public member getMember(int index){
        return FeatureMemebers.get(index);
    }

    public abstract member cloneMember(int index);

    public member getMember(String name){
        return getMember(getMemberIndex(name));
    }

    protected int getMemberIndex(String memberName){
        for (int i = 0; i < FeatureMemebers.size(); i++) {
            if(FeatureMemebers.get(i).getName().equals(memberName))
                return i;

        }
        return -1;
    }

    public void setFeatureMemebers(ArrayList<member> featureMemebers) {
        FeatureMemebers = featureMemebers;
    }

    public ArrayList<member> getFeatureMemebers() {
        return FeatureMemebers;
    }

    public void addMember(member member){
        FeatureMemebers.add(member);
    }

    public int getNumOfMember(){
        return FeatureMemebers.size();
    }

    public boolean isContainByName(member me) {
        for (int i = 0; i < FeatureMemebers.size(); i++) {
            if (me.getName().equals(FeatureMemebers.get(i).getName()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String ans = featureName + ": ";
        for (int i = 0; i < FeatureMemebers.size(); i++) {
            ans = ans + FeatureMemebers.get(i).getName() + ",";
        }
        return ans;
    }

    public boolean containsAllnames(feature fe2) {
        for (int i = 0; i < fe2.getFeatureMemebers().size(); i++) {
            if(!isContainByName(fe2.getMember(i)))
                return false;
        }
        return true;
    }
}
