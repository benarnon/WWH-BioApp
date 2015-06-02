package FeatureRelatedComponent;

/**
 * Created by user on 6/1/15.
 */
public class GeneUnitFeature extends feature {
    public GeneUnitFeature() {
        super("Target Genes Unit");
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
