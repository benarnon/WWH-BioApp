package FeatureRelatedComponent;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class SymptomsFeature extends feature {
    public SymptomsFeature() {
        super("Symptoms");
        FeatureMemebers = new ArrayList<Member>();
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
