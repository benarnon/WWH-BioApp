package Enumeration;




import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;

public class Quasi_biclique {
    public ArrayList<Sample> _samples; //X
    public FeatureArray _mutual_feature_array; //N(X)


    public Quasi_biclique(ArrayList<Sample> _samples, FeatureArray _mutual_geneunit) {
        this._samples = _samples;
        this._mutual_feature_array = _mutual_geneunit;
    }
}