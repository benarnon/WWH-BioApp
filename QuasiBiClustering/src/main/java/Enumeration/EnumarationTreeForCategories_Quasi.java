package Enumeration;


import BasicComponent.GeneUnit;
import BasicComponent.Sample;
import Mains.EnumParams;

import java.util.ArrayList;
import java.util.Stack;


//Preparing everything that is needed to run the enumeration process on specific category GO/KEGG
public class EnumarationTreeForCategories_Quasi
{
    ArrayList<Sample> _samplesInCat;
    ArrayList<GeneUnit> _geneUnits;

    int _uniqueGenesInCatNum; //only different ENSG
    int _total_num_of_samples;//unique - for p-value

    public ArrayList<Cluster> _clusters;//_cluster == good p_value
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
        ep.work(_samplesInCat,_data.getGeneUnitList());
        ArrayList<Quasi_biclique> cliques = ep._quasi_bicliques;

        System.out.println("NUMBER OF CLIQUES " + cliques.size());

        //Convert the bi_cliques into clusters data structure
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Quasi_biclique clique;
        for (int i = 0; i < cliques.size(); i++) {



        }

    }
    //Nested method need to be delete after finish testing
    public void nested_fill_pvalues(ArrayList<Cluster> clusters, DataForEnumaration data, int genesInCatNum){
        fill_pvalues(clusters, data,genesInCatNum);
    }
    private void fill_pvalues(ArrayList<Cluster> clusters, DataForEnumaration data, int genesInCatNum) {

    }
}
