package Enumeration;


import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;

import java.util.ArrayList;


public class Clusters_Holder
{
    ArrayList<Cluster> _clusters = new ArrayList<Cluster>();

    public void addCluster(FeatureArray featureArray, ArrayList<Sample> samples){
        _clusters.add(new Cluster(featureArray,samples));
    }
}