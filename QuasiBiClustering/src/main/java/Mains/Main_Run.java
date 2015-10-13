package Mains;

import Enumeration.DataForEnumaration;
import Enumeration.EnumarationTreeForCategories_Quasi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        //featureFiles.add(1,EnumParams.SymptomsF);

        DataForEnumaration data = new DataForEnumaration(featureFiles,sampleFile,matrixFile);
        System.out.println("DONE with data for enumeration");

        EnumarationTreeForCategories_Quasi tree = new EnumarationTreeForCategories_Quasi(data,data.getSamples());

        File file = new File("/home/user/IdeaProjects/WWH-BioApp_resources/matrix/mat5.csv");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(tree.printCluster());
        bw.close();


    }
}
