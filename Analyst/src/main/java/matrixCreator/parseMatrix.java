package matrixCreator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * parseMatrix class main goal is to parse the hdfs file after the MatrixCreator mapreduce phase.
 * input: hdfs file, healthy profile, gene list
 */
public class parseMatrix {
    private Map<String,Double> healthyProfile = new HashMap<>();
    private Matrix mat;

    public parseMatrix() {
        mat = new Matrix();
    }

    public Matrix parseFromHdfs(String geneListPath, String healthyPath,String hdfsMatrixFile) throws IOException {
        parseGeneList(geneListPath);
        fillMatrix(hdfsMatrixFile);
        parseHealthy(healthyPath);
        substractFromHealthy();
        return mat;
    }

    private void parseGeneList(String geneListPath) throws IOException {
        Path pt=new Path(geneListPath);
        FileSystem fs = FileSystem.get(new Configuration());
        if (!fs.exists(pt))
            System.out.println("Input file not found");
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
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
        Path pt=new Path(wwHmatrixPath);
        FileSystem fs = FileSystem.get(new Configuration());
        if (!fs.exists(pt))
            System.out.println("Input file not found");
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        String line;
        FileReader reader = new FileReader(wwHmatrixPath);
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
    //Substract genes wich are no significant
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
                    if (currentAbundance - healthyAbundance < 1)
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double) 0);
                    else {
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double)1);
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

            else {
                for (Map.Entry<String, Integer> entry2 : samples.entrySet()) {
                    double currentAbundance = mat.getAbundance(entry.getKey(), entry2.getKey());
                    if (currentAbundance < 1)
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double) 0);
                    else {
                        mat.setAbundance(entry.getKey(), entry2.getKey(), (double) 1);
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

    //Without substratcing genes
    private void substractFromHealthy2() {
        String ans = "\t";
        Map<String, Integer> samples = mat.getSamples();
        Map<String, Integer> genes = mat.getGenes();
        for (Map.Entry<String, Integer> entry : genes.entrySet()) {

            if (healthyProfile.get(entry.getKey()) != null) {
                double healthyAbundance = healthyProfile.get(entry.getKey());
                for (Map.Entry<String, Integer> entry2 : samples.entrySet()) {
                    double currentAbundance = mat.getAbundance(entry.getKey(), entry2.getKey());
                    mat.setAbundance(entry.getKey(), entry2.getKey(), currentAbundance - healthyAbundance);
                }
            }
        }
    }


}

