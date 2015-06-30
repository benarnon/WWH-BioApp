package Mains;

import Enumeration.DataForEnumaration;
import Enumeration.EnumarationTreeForCategories_Quasi;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 5/10/15.
 */
public class Main_Run {
    public static void main(String[] args) throws Exception {
        String sampleFile = EnumParams.SamplsF;
        String matrixFile = EnumParams.MatrixF;
        ArrayList<String> featureFiles = new ArrayList<>();
        featureFiles.add(0,EnumParams.GenomesF);
        featureFiles.add(1,EnumParams.SymptomsF);
        DataForEnumaration data = new DataForEnumaration(featureFiles,sampleFile,matrixFile);
        System.out.println("DONE with data for enumeration");

        EnumarationTreeForCategories_Quasi tree = new EnumarationTreeForCategories_Quasi(data,data.getSamples());
        System.out.println(tree.printCluster());

    }
}
