import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/8/15.
 */
public class BiclusterResults {
    List<Bicluster> biclustList;
    String info;
    int bestIndex =-1;
    int count=0;
    //parse the R-script bicluster results.
    public BiclusterResults(String path,MatrixCreator matrixCreator) {
        float bestScore = -1;
        String regex = "\\d+";
        biclustList = new ArrayList<Bicluster>();
        PathogenScoreList pathogenScoreList = matrixCreator.getPathogenScoreList();
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
                if (line.split(" ")[0].equals("CC")){
                    info = line;
                }
                else if(line.split("\t").length ==2 && line.split("\t")[0].matches(regex) && line.split("\t")[1].matches(regex)){
                    Bicluster tmp =new Bicluster(Integer.parseInt(line.split("\t")[0]),Integer.parseInt(line.split("\t")[1]));
                    line = br.readLine();
                    String[] splitPathogens = line.split("\t");
                    line = br.readLine();
                    String[] splitSamples = line.split("\t");
                    tmp.setPathogens(splitPathogens);
                    tmp.setSamples(splitSamples);
                    fillCluster(tmp, pathogenScoreList);
                    tmp.setClusterScore();
                    biclustList.add(tmp);
                    if(bestIndex == -1) {
                        bestIndex = 0;
                        bestScore = tmp.getClusterScore();
                    }
                    else if(tmp.getClusterScore() > bestScore){
                        bestIndex = count;
                        bestScore = tmp.getClusterScore();
                    }
                    count++;
                }
                else if(line.equals("")){
                    System.out.println("End of Bicluster Results");
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

    }
    //This method fill the matrix score of the bicluster
    private void fillCluster(Bicluster tmp, PathogenScoreList pathogenScoreList) {
        for (int i = 0; i <tmp.getPathogens().length ; i++) {
            for (int j = 0; j <tmp.getSamples().length ; j++) {
                tmp.addScore(i,j,pathogenScoreList.getScore(tmp.getPathogen(i),tmp.getSample(j)));
            }
        }
    }

    @Override
    public String toString() {
        String ans = info +"\n";
        int index = 0;
        for (Bicluster tmp: biclustList){
            if (index == bestIndex){
                ans = ans +"Best bicluster ";
            }
            ans = ans + tmp.toString();
            index++;
        }
        return ans;
    }

    public void createBiclusterFile(String biclustPath){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(biclustPath + "/Bicluster.txt", "UTF-8");
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
}
