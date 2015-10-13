package Enumeration;




import FeatureRelatedComponent.featureArray;
import BasicComponent.Sample;

import java.util.ArrayList;

public class Quasi_biclique {
    public ArrayList<Sample> _samples; //X
    public featureArray _mutual_feature_array; //N(X)


    public Quasi_biclique(ArrayList<Sample> _samples, featureArray _mutual_geneunit) {
        this._samples = _samples;
        this._mutual_feature_array = _mutual_geneunit;
    }
}