import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class MatrixCreator {


    public PathogenScoreList getPathogenScoreList() {
        return pathogenScoreList;
    }

    public void setPathogenScoreList(PathogenScoreList pathogenScoreList) {
        this.pathogenScoreList = pathogenScoreList;
    }

    private PathogenScoreList pathogenScoreList;

    public MatrixCreator() {

    }




    public void CreateMatrixFile2(String MatrixPath){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(MatrixPath + "/matrix.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String ans = toString();
        assert writer != null;
        writer.println(ans);

        writer.close();
    }


    public void CreateMatrix2(String SamplesPath){
        File dir = new File(SamplesPath);
        FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        };
        File[] directoryListing = dir.listFiles(directoryFilter);
        if (directoryListing == null)
            return;
        pathogenScoreList = new PathogenScoreList(directoryListing.length);
        for (int i = 0; i < directoryListing.length; i++) {
            BufferedReader br = null;
            try {

                br = new BufferedReader(new FileReader(directoryListing[i]));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                int count = -1;
                String line = null;
                if (br != null) {
                    line = br.readLine();
                }
                while (line != null) {
                    if (count == -1) {
                        pathogenScoreList.addSample(line,i);
                        count++;
                        line = br.readLine();
                    }
                    //Line contains score, the format is ClusterID\Pathogen_name    Score
                    else {
                        String name = line.split("\t")[0].split(Pattern.quote("\\"))[1];
                        float score = Float.parseFloat(line.split("\t")[1]);
                        //The first time we see this pathogen
                        if (pathogenScoreList.isContain(name) == -1) {
                            pathogenScoreList.addPathogen(name);
                            if(score < 0.2)
                                pathogenScoreList.addScore(name, score, i);
                            else
                                pathogenScoreList.addScore(name, score, i);
                        } else {
                            if(score < 0.2)
                                pathogenScoreList.addScore(name, score, i);
                            else
                                pathogenScoreList.addScore(name, score, i);
                        }
                        line = br.readLine();
                    }
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


        }


    }
    @Override
    public String toString() {
        String ans = "";
        for (String aSampleList : pathogenScoreList.getSampleList()) {
            ans = ans + "\t" + aSampleList;

        }
        ans = ans + "\n"+ pathogenScoreList.toString();
        return ans;
    }
}
