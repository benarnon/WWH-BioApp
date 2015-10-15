import biclusterCreator.ParseBicluster;
import matrixCreator.MatrixMapred;
import matrixCreator.parseMatrix;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import treeCreator.TreeCreator;

import java.io.File;

import java.io.IOException;

/**
 * Created by user on 10/13/15.
 */
public class Main {
    /*
     * Client side from the moment the wwh process finished
     * It first the matrix file based on the results of MatrixCreator
     * Then it run isa bicluster script on R. The reuslts we transform into JSON files
     * We then run the TreeCreator on each biclutser and create new json file for each tree.
     */
    public static final String REFERENCE_INDEX_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/inputFiles/RefIndex.txt";
    public static final String HEALTHY_PROFILE_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/inputFiles/HealthyProfile";
    public static final String HDFS_MATRIX_FILE_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/inputFiles/NewHDFSmatrix";
    public static final String MATRIXCREATOR_OUTPUT_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/ParseMatrix/RanMatrix.csv";
    public static final String BICLUSTERS_FILE_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/ParseBicluster/R_Bicluter_Results";
    public static final String R_BICLUSTER_PATH = "/home/user/IdeaProjects/WWH-BioApp_resources/ParseBicluster";
    public static final String GENES_JSON_FILE = "/home/user/IdeaProjects/WWH-BioApp_resources/ParseBicluster/gene-units.json";
    public static final String SAMPLE_JSON_FILE ="/home/user/IdeaProjects/WWH-BioApp_resources/inputFiles/samples.json";


    public static void main(String[] args) throws Exception {
        //MatrixMapred.MatrixCreator();

        FileUtils.cleanDirectory(new File("/home/user/IdeaProjects/WWH-BioApp_resources/ParseBicluster/"));
        FileUtils.cleanDirectory(new File("/home/user/IdeaProjects/WWH-BioApp_resources/ParseMatrix/"));
        //Parse the WWH results of MatrixCreator
        System.out.println("Parse the HDFS file");
        parseMatrix parseMatrix = new parseMatrix();
        parseMatrix.parseFromHdfs(REFERENCE_INDEX_PATH,HEALTHY_PROFILE_PATH,HDFS_MATRIX_FILE_PATH, MATRIXCREATOR_OUTPUT_PATH,GENES_JSON_FILE);
        Process callSeqTrak = null;
        try {
            //calling r script to run seqTrak, an wait for the script
            //to finish before proceeding
            System.out.println("R script");
            callSeqTrak = Runtime.getRuntime().exec("Rscript " + "/home/user/IdeaProjects/WWH-BioApp/Analyst/src/main/java/ISAClustN.R " + MATRIXCREATOR_OUTPUT_PATH + " " + R_BICLUSTER_PATH );
            callSeqTrak.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ParseBicluster parseBicluster = new ParseBicluster();
        parseBicluster.parseBicluster(BICLUSTERS_FILE_PATH,SAMPLE_JSON_FILE, R_BICLUSTER_PATH);

        for(int i = 0; i < 3; i++){
            TreeCreator tree = new TreeCreator();
            tree.CreateTree("/home/user/IdeaProjects/WWH-BioApp_resources/WWH-results/app/biclusters/bicluster" + i + ".json","/home/user/IdeaProjects/WWH-BioApp_resources/TreeCreator/midFiles/"
                    , "/home/user/IdeaProjects/WWH-BioApp_resources/WWH-results/app/views/maps/","/home/user/IdeaProjects/WWH-BioApp/Analyst/src/main/java/SeqTrakTreeCreator.R", new Integer(i).toString());
        }



    }
}
