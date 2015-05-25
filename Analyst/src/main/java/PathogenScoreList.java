import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/4/15.
 */
public class PathogenScoreList {
    private List<PathogenScores> PathogenList;
    private String[] sampleList;
    private int numOfFiles;
    public int getNumOfFiles() {
        return numOfFiles;
    }

    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }



    public PathogenScoreList(int numOfFiles) {
        PathogenList = new ArrayList<PathogenScores>();
        this.numOfFiles = numOfFiles;
        this.sampleList = new String[numOfFiles];
    }

    public void addPathogen(String name){
        PathogenScores path = new PathogenScores(name,new float[numOfFiles]);
        PathogenList.add(path);
    }

    public void addSample(String sample,int index){
        sampleList[index] = sample;
    }

    public String getSample(int index){
        return sampleList[index];
    }

    public String[] getSampleList() {
        return sampleList;
    }

    public void setSampleList(String[] sampleList) {
        this.sampleList = sampleList;
    }

    public int getSampleIndex(String sample){
        int ans = -1;
        for (int i = 0; i < sampleList.length; i++) {
            if(sampleList[i].equals(sample))
                return i;
        }
        return ans;
    }

    //return the index of the pathogen if its locate in the Pathogen list -1 if it is not
    public int isContain(String name){
        int res = 0;
        for (PathogenScores path: PathogenList){
            if(path.getName().equals(name))
                return res;
            res++;
        }
        return -1;
    }

    public void addScore(String name,float score,int sampleIndex){
        if(isContain(name)!= -1){
            PathogenList.get(isContain(name)).addScore(score,sampleIndex);
        }
    }

    public PathogenScores getPathogenScore(String name){
        return PathogenList.get(isContain(name));
    }

    @Override
    public String toString() {
        String ans = "";
        for (PathogenScores pathogenScores: PathogenList){
            ans = ans + pathogenScores.getName();
            for (int i = 0; i <numOfFiles ; i++) {
                ans = ans + "\t" + pathogenScores.getScore(i);

            }
            ans = ans+"\n";
        }
        return ans;
    }

    public float getScore(String pathogen, int indexSample){
        return getPathogenScore(pathogen).getScore(indexSample);
    }

    public float getScore(int pathogenIndex,int sampleIndex){
        return PathogenList.get(pathogenIndex).getScore(sampleIndex);
    }

    public float getScore(String pathogen, String sample) {
        int sampleIndex = getSampleIndex(sample);
        return getPathogenScore(pathogen).getScore(sampleIndex);

    }
}
