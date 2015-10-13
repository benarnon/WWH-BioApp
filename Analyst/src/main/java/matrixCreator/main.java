package matrixCreator;

import matrixCreator.parseMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static matrixCreator.MatrixMapred.MatrixCreator;

/**
 * Created by user on 10/11/15.
 */
public class main {
    public static void main(String[] args) throws Exception {
        //MatrixCreator();
        parseMatrix parseMatrix = new parseMatrix();
        String ans = parseMatrix.parseFromHdfs("/matrixOut/RefIndex.txt","/matrixOut/HealthyProfile","/matrixOut/part-r-00000").toString();

        File file = new File("/home/user/IdeaProjects/WWH-BioApp_resources/Matrix/ResultMatrix.csv");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(ans);
        bw.close();

    }
}
