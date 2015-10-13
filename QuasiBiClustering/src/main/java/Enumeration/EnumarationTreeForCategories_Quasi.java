package Enumeration;


import FeatureRelatedComponent.featureArray;
import BasicComponent.Sample;

import java.util.ArrayList;


//Preparing everything that is needed to run the enumeration process on specific category GO/KEGG
public class EnumarationTreeForCategories_Quasi
{
    ArrayList<Sample> _samplesInCat;
    featureArray _featureArray = new featureArray();

    int _uniqueGenesInCatNum; //only different ENSG
    int _total_num_of_samples;//unique - for p-value

    public ArrayList<Cluster> _clusters = new ArrayList<Cluster>();//_cluster == good p_value
    public int _allClusters,_uniqueClusters;

    public int numberOfGenes_allModules =0 ;
    public int numberOfGenes_allModules_S=0;
    public int numberOfGenes_uniqueModules=0;

    public int numberOfHmirs_allModules =0;
    public int numberOfHmirs_allModules_S=0;
    public int numberOfHmirs_uniqueModules=0;

    public int numberOfVmirs_allModules=0;
    public int numberOfVmirs_allModules_S=0;
    public int numberOfVmirs_uniqueModules=0;

    DataForEnumaration _data;
    //Category _category;


    public EnumarationTreeForCategories_Quasi(DataForEnumaration data, ArrayList samplesInCat) throws Exception {

        _data = data;

        _samplesInCat = samplesInCat;
        _total_num_of_samples = data.getSamples().size();

        EnumarationProcess_Quasi ep = new EnumarationProcess_Quasi();
        ep.work(_samplesInCat,_data.getFeatureArray());
        ArrayList<Quasi_biclique> cliques = ep._quasi_bicliques;

        System.out.println("NUMBER OF CLIQUES " + cliques.size());

        //Convert the bi_cliques into clusters data structure
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Quasi_biclique clique;
        for (int i = 0; i < cliques.size(); i++) {
            clique = cliques.get(i);
            Cluster cluster = new Cluster(clique._mutual_feature_array,clique._samples);
            _clusters.add(cluster);
        }

    }
    //Nested method need to be delete after finish testing
    public void nested_fill_pvalues(ArrayList<Cluster> clusters, DataForEnumaration data, int genesInCatNum){
        fill_pvalues(clusters, data,genesInCatNum);
    }
    private void fill_pvalues(ArrayList<Cluster> clusters, DataForEnumaration data, int genesInCatNum) {

    }

    public ArrayList<Cluster> get_clusters() {
        return _clusters;
    }

    public String printCluster() {
        String ans = "";
        for (int i = 0; i <_clusters.size() ; i++) {
            int j = i+1;
            ans = ans +"Cluster number " + j + "\n" + _clusters.get(i).toString() +"\n";
        }

        return  ans;
    }
}
