package FeatureRelatedComponent;

import Mains.EnumParams;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class GeneUnitFeature extends feature {
    public GeneUnitFeature() {
        super(EnumParams.GenesFeatureName);
        FeatureMemebers = new ArrayList<Member>();
    }

    @Override
    public Member cloneMember(int index) {
        GeneUnit gene = (GeneUnit)FeatureMemebers.get(index);

        Member ans = new GeneUnit(gene.getName(),gene.getLength(),gene.getDbList());
        return ans;
    }

    @Override
    public void addMember(Member member) {
        if (member instanceof GeneUnit){
            FeatureMemebers.add(member);
        }
        else{
            throw new IllegalArgumentException("SypmtomsArray should contain only Symptoms objects");
        }
    }


}
