package Enumeration;


import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;

public class Cluster
{
    public ArrayList<GeneUnit> _genesunit = new ArrayList<GeneUnit>();
    public ArrayList<Sample> _samples = new ArrayList<Sample>();

    public double _p_val;
    public int _t; //t - total number of targets (unique)
    public int _tg;//tg - number of targets in category (unique)
    public int _n;
    public int _g;

    public Cluster(ArrayList<GeneUnit> _genesunit, ArrayList<Sample> _samples) {
        this._genesunit = _genesunit;
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
}
