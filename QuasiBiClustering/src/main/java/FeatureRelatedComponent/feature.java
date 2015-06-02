package FeatureRelatedComponent;

import java.util.ArrayList;

/**
 * Created by user on 5/31/15.
 */
public abstract class feature {
    protected ArrayList<Member> FeatureMemebers = new ArrayList<Member>();
    protected  String featureNAme;

    public feature(String featureNAme) {
        this.featureNAme = featureNAme;
    }

    public String getFeatureName() {
        return featureNAme;
    }

    public void setFeatureName(String featureNAme) {
        this.featureNAme = featureNAme;
    }

    public Member getMember(int index){
        return FeatureMemebers.get(index);
    }
    public Member getMember(String name){
        return getMember(getMemberIndex(name));
    }

    protected int getMemberIndex(String memberName){
        for (int i = 0; i < FeatureMemebers.size(); i++) {
            if(FeatureMemebers.get(i).getName().equals(memberName))
                return i;

        }
        return -1;
    }

    public void setFeatureMemebers(ArrayList<Member> featureMemebers) {
        FeatureMemebers = featureMemebers;
    }

    public ArrayList<Member> getFeatureMemebers() {
        return FeatureMemebers;
    }

    public void addMember(Member member){
        FeatureMemebers.add(member);
    }
}
