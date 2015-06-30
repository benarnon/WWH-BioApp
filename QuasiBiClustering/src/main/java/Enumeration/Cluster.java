package Enumeration;


import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;

public class Cluster
{
    public FeatureArray _featureArray = new FeatureArray();
    public ArrayList<Sample> _samples = new ArrayList<Sample>();

    public double _p_val;
    public int _t; //t - total number of targets (unique)
    public int _tg;//tg - number of targets in category (unique)
    public int _n;
    public int _g;

    public Cluster(FeatureArray _featureArray, ArrayList<Sample> _samples) {
        this._featureArray = _featureArray;
        this._samples = _samples;
    }

    public void set_p_val_tg_t(double p_val, int tg, int t , int n, int g)
    {
        this._p_val = p_val;
        this._tg = tg;
        this._t = t;
        this._n = n;
        this._g = g;
    }

    @Override
    public String toString() {
        String ans = "Samples in cluster: ";
        for (int i = 0; i < _samples.size(); i++) {
            ans = ans + _samples.get(i) + ",";
        }
        ans = ans + "\nFeatures:";
        for (int i = 0; i < _featureArray.getNumOfFeatures(); i++) {
            ans = ans +"\n"+  _featureArray.getFeature(i).toString();
        }

        return ans;
    }
}
