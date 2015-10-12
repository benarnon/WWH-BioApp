package matrixCreator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * parseMatrix class main goal is to parse the hdfs file after the MatrixCreator mapreduce phase.
 * input: hdfs file, healthy profile, gene list
 */
public class parseMatrix {
    private Map<String,Double> healthyProfile = new HashMap<>();
    private matrix mat;

    public parseMatrix() {
        mat = new matrix();
    }

    public matrix parseFromHdfs(int numOfSamples, String geneListPath, String healthyPath,String WWHmatrixPath) throws IOException {
        parseGeneList(geneListPath);
        fillMatrix(WWHmatrixPath);
        parseHealthy(healthyPath);
        substractFromHealthy();
        return mat;
    }

    private void parseGeneList(String geneListPath) throws IOException {
        FileReader reader = new FileReader(geneListPath);
        BufferedReader br = new BufferedReader(reader);
        String line;
        int index = 0;
        while((line = br.readLine()) != null){
            if(line.split("\t").length == 2){
                mat.setGene(line.split("\t")[0],index);
                index++;
            }
        }
    }

    private void fillMatrix(String wwHmatrixPath) throws IOException {
        FileReader reader = new FileReader(wwHmatrixPath);
        BufferedReader br = new BufferedReader(reader);
        String line;
        int sampleIndex = 0;
        String currentSample = "";
        mat.setMatrixSize(mat.getNumOfGenes(),99); //TODO number of samples is not dynamic
        while((line = br.readLine()) !=null ){
            if(!line.equals("") && line.split("\t").length == 1){
                mat.setSample(line,sampleIndex);
                currentSample = line;
                sampleIndex++;
            }
            else if(line.split("\t").length == 3){
                mat.setAbundance(line.split("\t")[1],currentSample,Double.parseDouble(line.split("\t")[2]));
            }
            else if(line.split("\t").length == 2){
                mat.setAbundance(line.split("\t")[0],currentSample,Double.parseDouble(line.split("\t")[1]));
            }
        };
    }

    private void parseHealthy(String healthyPath) throws IOException {
        FileReader reader = new FileReader(healthyPath);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while((line = br.readLine()) != null){
            if(line.split("\t").length == 3){
                healthyProfile.put(line.split("\t")[1],Double.parseDouble(line.split("\t")[2]));
            }
            else if(line.split("\t").length == 2){
                healthyProfile.put(line.split("\t")[0], Double.parseDouble(line.split("\t")[1]));
            }
        }
    }

    private void substractFromHealthy() {
        String ans = "\t";
        Map<String, Integer> samples = mat.getSamples();
        Map<String, Integer> genes = mat.getGenes();
        int counter,num = 0;
        for (Map.Entry<String, Integer> entry : genes.entrySet()) {
            counter = 0;

            if (healthyProfile.get(entry.getKey()) != null) {
                double healthyAbundance = healthyProfile.get(entry.getKey());
                for (Map.Entry<String, Integer> entry2 : samples.entrySet()) {
                    double currentAbundance = mat.getAbundance(entry.getKey(), entry2.getKey());
                    if (currentAbundance - healthyAbundance < 0.4)
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double) 0);
                    else {
                        mat.setAbundance(entry.getKey(), entry2.getKey(), currentAbundance - healthyAbundance);
                        counter++;
                    }
                }
                if (counter > 50) {
                    num++;
                    System.out.println("delete row " + num);
                    for (Map.Entry<String, Integer> entry2 : samples.entrySet()) {
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double) 0);
                    }
                }
            }

        }
    }
}
