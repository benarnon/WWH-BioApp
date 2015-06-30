package FeatureRelatedComponent;

import Mains.EnumParams;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class SymptomsFeature extends feature {
    public SymptomsFeature() {
        super(EnumParams.SymptomsFeatureName);
        FeatureMemebers = new ArrayList<Member>();
    }

    @Override
    public Member cloneMember(int index) {
        Symptom symptom = (Symptom)FeatureMemebers.get(index);

        Member ans = new Symptom(symptom.getName());
        return ans;
    }

    @Override
    public void addMember(Member member) {
        if (member instanceof Symptom){
            FeatureMemebers.add(member);
        }
        else{
            throw new IllegalArgumentException("SypmtomsArray should contain only Symptoms objects");
        }
    }
}
