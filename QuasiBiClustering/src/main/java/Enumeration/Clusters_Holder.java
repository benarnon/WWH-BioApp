package Enumeration;


import FeatureRelatedComponent.featureArray;
import BasicComponent.Sample;

import java.util.ArrayList;


public class Clusters_Holder
{
    ArrayList<Cluster> _clusters = new ArrayList<Cluster>();

    public void addCluster(featureArray featureArray, ArrayList<Sample> samples){
        _clusters.add(new Cluster(featureArray,samples));
    }
}