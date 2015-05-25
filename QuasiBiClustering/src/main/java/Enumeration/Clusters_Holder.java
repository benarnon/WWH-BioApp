package Enumeration;


import BasicComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;


public class Clusters_Holder
{
    ArrayList<Cluster> _clusters = new ArrayList<Cluster>();

    public void addCluster(ArrayList<GeneUnit> genes_unit, ArrayList<Sample> samples){
        _clusters.add(new Cluster(genes_unit,samples));
    }
}