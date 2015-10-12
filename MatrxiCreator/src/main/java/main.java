import matrixCreator.parseMatrix;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static matrixCreator.matrixMapred.MatrixCreator;

/**
 * Created by user on 10/11/15.
 */
public class main {
    public static void main(String[] args) throws Exception {
        //MatrixCreator();
        parseMatrix parseMatrix = new parseMatrix();
        String ans = parseMatrix.parseFromHdfs(100,"/home/user/IdeaProjects/WWH-BioApp_resources/matrix/RefIndex.txt","/home/user/IdeaProjects/WWH-BioApp_resources/matrix/HealthyProfile","/home/user/IdeaProjects/WWH-BioApp_resources/matrix/outputMat").toString();

        File file = new File("/home/user/IdeaProjects/WWH-BioApp_resources/matrix/mat.csv");

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
