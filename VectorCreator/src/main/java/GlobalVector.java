import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by user on 3/5/15.
 */
public class GlobalVector {
    private String SampleName;
    private List<GlobalResTuple> ResGlobalVector;

    public GlobalVector(String sampleName) {
        SampleName = sampleName;
        ResGlobalVector = new ArrayList<GlobalResTuple>();
    }

    public GlobalVector(){
        ResGlobalVector = new ArrayList<GlobalResTuple>();
    }

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String sampleName) {
        SampleName = sampleName;
    }

    public List<GlobalResTuple> getResGlobalVector() {
        return ResGlobalVector;
    }

    public void setResGlobalVector(List<GlobalResTuple> resGlobalVector) {
        ResGlobalVector = resGlobalVector;
    }

    public void setTuple(GlobalResTuple globalResTuple){
        ResGlobalVector.add(globalResTuple);
    }

    public GlobalResTuple getResTuple(int index){
        if(index >= VectorSize())
            return null;
        return ResGlobalVector.get(index);
    }

    public GlobalResTuple getResTuple(String pathogen){
        if(isContain(pathogen)!=-1)
            return ResGlobalVector.get(isContain(pathogen));
        else
            return null;
    }

    public int isContain(String pathogen) {
        int res = 0;
        for (GlobalResTuple tuple: ResGlobalVector){
            if (tuple.getPathogen().equals(pathogen))
                return res;
            res++;
        }
        return -1;
    }

    public int VectorSize(){
        return ResGlobalVector.size();
    }


    public GlobalVector path2vector(String path){
        GlobalVector res;
        res = new GlobalVector(SampleName);
        String ClusterID = "";
        String pathogen = "";
        float score;
        String SampleName="";

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
                    SampleName = line;
                    res.setSampleName(line);
                }

                if(line.split("\t").length != 1) {
                    ClusterID = (line.split("\t")[0]).split(Pattern.quote("\\"))[0];
                    pathogen =  (line.split("\t")[0]).split(Pattern.quote("\\"))[1];
                    score = Float.parseFloat(line.split("\t")[1]);
                    GlobalResTuple tmp = new GlobalResTuple(pathogen,score,ClusterID);
                    res.setTuple(tmp);
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
        return res;
    }

    @Override
    public String toString() {
        String ans = SampleName +"\n";
        for (GlobalResTuple tmp:ResGlobalVector){
            ans = ans + tmp.getClusterID() +"\\"+ tmp.getPathogen() +"\t"+ tmp.getScore() +"\n";
        }
        return ans;

    }

    public void SubstractHealthy(GlobalVector healthy){
        for (int i = 0; i < ResGlobalVector.size() ; i++) {
            GlobalResTuple tmp = ResGlobalVector.get(i);
            if(healthy.isContain(tmp.getPathogen())!=-1){
                ResGlobalVector.get(i).setScore(tmp.getScore() - healthy.getResTuple(tmp.getPathogen()).getScore());
            }
        }

        for (int i = 0; i < healthy.VectorSize(); i++) {
            GlobalResTuple tmp = healthy.getResTuple(i);
            if(isContain(tmp.getPathogen())==-1){
                setTuple(new GlobalResTuple(tmp.getPathogen(),0-tmp.getScore(),tmp.getClusterID()));
            }
        }


    }
}
