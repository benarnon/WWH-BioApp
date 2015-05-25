package Mains;

import Enumeration.DataForEnumaration;
import Enumeration.EnumarationTreeForCategories_Quasi;

import java.io.IOException;

/**
 * Created by user on 5/10/15.
 */
public class Main_Run {
    public static void main(String[] args) throws Exception {
        String genomesFile = EnumParams.GenomesF;
        String sampleFile = EnumParams.SamplsF;
        String matrixFile = EnumParams.MatrixF;

        DataForEnumaration data = new DataForEnumaration(genomesFile,sampleFile,matrixFile);
        System.out.println("DONE with data for enumeration");

        EnumarationTreeForCategories_Quasi tree = new EnumarationTreeForCategories_Quasi(data,data.getSamples());
        System.out.println("checking git");

    }
}
