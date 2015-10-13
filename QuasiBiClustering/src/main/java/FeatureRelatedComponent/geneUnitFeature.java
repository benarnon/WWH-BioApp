package FeatureRelatedComponent;

import Mains.EnumParams;

import java.util.ArrayList;

/**
 * Created by user on 6/1/15.
 */
public class geneUnitFeature extends feature {
    public geneUnitFeature() {
        super(EnumParams.GenesFeatureName);
        FeatureMemebers = new ArrayList<member>();
    }

    @Override
    public member cloneMember(int index) {
        geneUnit gene = (geneUnit)FeatureMemebers.get(index);

        member ans = new geneUnit(gene.getName(),gene.getLength());
        return ans;
    }

    @Override
    public void addMember(member member) {
        if (member instanceof geneUnit){
            FeatureMemebers.add(member);
        }
        else{
            throw new IllegalArgumentException("SypmtomsArray should contain only Symptoms objects");
        }
    }


}
