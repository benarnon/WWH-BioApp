import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/2/15.
 * VecorUnit class will be replaced by the global reudcer.
 * His task is to aggregate all the local vectors to one vector and upload it to the origin cluster
 */
public class VectorUnit {
    private String SampleName;
    private GlobalVector globalVector;

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String sampleName) {
        SampleName = sampleName;
    }

    public VectorUnit(String SampleName){
        this.SampleName = SampleName;
        this.globalVector = new GlobalVector();
    }

    public void CreateFile2(String healthyPath,String DirPath,String finalPath,String SampleName){
        File dir = new File(DirPath);
        File[] directoryListing = dir.listFiles();
        globalVector.setSampleName(SampleName);
        for (File tmp: directoryListing){
            if (!tmp.isDirectory() & tmp.getName().toLowerCase().endsWith(".txt") & tmp.getName().startsWith(SampleName) )
                AddLocal2Global(tmp.getPath());
        }
        GlobalVector healthy = globalVector.path2vector(healthyPath);
        globalVector.SubstractHealthy(healthy);
        PrintWriter writer = null;
        String localVecString = "";
        try {
            writer = new PrintWriter(finalPath + "/" +SampleName +"-GlobalVector" , "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.print(globalVector.toString());
        if (writer != null) {
            writer.close();
        }
    }

    public void AddLocal2Global(String path) {
        LocalVector tmp = Path2Vector(path);
        String ClusterID = tmp.getCluserID();
        for (int i = 0; i < tmp.GetSizeResVector(); i++) {
            float score = Float.parseFloat(tmp.GetResTuple(i).getScore());
            String pathogen = tmp.GetResTuple(i).getPathogen();
            globalVector.setTuple(new GlobalResTuple(pathogen,score,ClusterID));
        }
    }

    private LocalVector Path2Vector(String path) {
        LocalVector res;
        List<ResTuple> TmpVector = new ArrayList<ResTuple>();
        String ClusterID= "";
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String line = null;
            if (br != null) {
                line = br.readLine();
            }

            while (line != null) {
                if(line.split("\t").length == 1){
                    //Title of local vector file will be "ClusterID/SampleName": example Egypt/Sample1
                    ClusterID = line.split("/")[0];
                }

                if(line.split("\t").length != 1) {
                    TmpVector.add(new ResTuple(line.split("\t")[0], line.split("\t")[1]));
                }

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        res = new LocalVector(ClusterID,TmpVector);
        return res;
    }


}
