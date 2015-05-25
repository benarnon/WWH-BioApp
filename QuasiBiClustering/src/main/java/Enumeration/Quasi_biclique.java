package Enumeration;




import BasicComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;

public class Quasi_biclique {
    public ArrayList<Sample> _samples; //X
    public ArrayList<GeneUnit> _mutual_geneunit; //N(X)


    public Quasi_biclique(ArrayList<Sample> _samples, ArrayList<GeneUnit> _mutual_geneunit) {
        this._samples = _samples;
        this._mutual_geneunit = _mutual_geneunit;
    }
}