package matrixCreator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * parseMatrix class's  goal is to parse the hdfs file after the MatrixCreator mapreduce phase.
 * input: hdfs file, healthy profile, gene list
 */
public class parseMatrix {
    private Map<String,Double> healthyProfile = new HashMap<>();
    private Matrix mat;

    public parseMatrix() {
        mat = new Matrix();
    }

    public void parseFromHdfs(String geneListPath, String healthyPath,String hdfsMatrixFile, String  outputMatrixPath, String geneunitsJsonPath) throws IOException {
        parseGeneList(geneListPath,geneunitsJsonPath);
        fillMatrix(hdfsMatrixFile);
        parseHealthy(healthyPath);
        substractFromHealthy2();

        String ans = mat.toString();

        File file = new File(outputMatrixPath);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(ans);
        bw.close();
        System.out.println("Finish to write matrix file " + outputMatrixPath);
    }

    private void parseGeneList(String geneListPath,String geneunitsJsonPath) throws IOException {
        JSONArray geneUnits = new JSONArray();
        JSONObject geneUnit = new JSONObject();
        Path pt=new Path(geneListPath);
        FileSystem fs = FileSystem.get(new Configuration());
        if (!fs.exists(pt))
            System.out.println("Input file not found");
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        String line;
        int index = 0;
        while((line = br.readLine()) != null){
            if(line.split("\t").length == 2){
                geneUnit = new JSONObject();
                String description = "";
                for (int i = 1; i < line.split("\t")[0].split(" ").length; i++) {
                    description = description + " " + line.split("\t")[0].split(" ")[i];
                }
                geneUnit.put("id",line.split("\t")[0].split(" ")[0]);
                geneUnit.put("description",description);
                geneUnits.add(geneUnit);
                mat.setGene(line.split("\t")[0],index);
                index++;
            }
        }
        FileWriter outputFile = new FileWriter(geneunitsJsonPath);
        outputFile.write(geneUnits.toJSONString());
        outputFile.flush();
        outputFile.close();
        System.out.println("Finish to write geneunits json file " + geneunitsJsonPath);

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
        private void substractFromHealthy2() {
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

