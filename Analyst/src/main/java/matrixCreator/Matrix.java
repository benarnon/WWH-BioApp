package matrixCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 10/11/15.
 */
public class Matrix {
    private double[][] matrix;

    private Map<String ,Integer> genes = new HashMap<>();

    private Map<String,Integer> samples = new HashMap<>();
    public Matrix() {}

    public void setMatrixSize(int numOfGenes, int numOfSamples) {matrix = new double[numOfGenes][numOfSamples];}

    public void setAbundance(String geneId, String sampleId, Double abundance){
        int gene = getGene(geneId);
        int sample = getSample(sampleId);
        matrix[gene][sample] = abundance;
    }

    public double getAbundance(String geneId, String sampleId){
        return matrix[getGene(geneId)][getSample(sampleId)];
    }

    public double getAbundance(int geneIndex, int sampleIndex){
        return matrix[geneIndex][sampleIndex];
    }

    public void setGene(String gene,int index){
        genes.put(gene,index);
    }

    public void setSample(String sample,int index){
        samples.put(sample,index);
    }

    public int getGene(String geneId){
        return genes.get(geneId);
    }

    public Map<String, Integer> getSamples() {
        return samples;
    }

    public Map<String, Integer> getGenes() {
        return genes;
    }

    private int getSample(String sampleId) {
        return samples.get(sampleId);
    }

    public int getNumOfGenes(){
        return genes.size();
    }

    public int getNumOfSamples(){
        return samples.size();
    }

    @Override
    public String toString() {
        String ans ="\t";
        for (Map.Entry<String, Integer> entry : samples.entrySet())
        {
            ans = ans + entry.getKey() +"\t";
        }
        ans = ans +"\n";
        for (Map.Entry<String, Integer> entry : genes.entrySet())
        {
            ans = ans +  (entry.getKey()) +"\t";

            for (Map.Entry<String,Integer> entry2 : samples.entrySet())
            {
                ans = ans + (matrix[entry.getValue()][entry2.getValue()]) +"\t";
            }

            ans = ans +"\n";
        }

        return ans;
    }

    public String matrixTojson(String outputJsonFile) throws IOException {
        //outbreak test results
        JSONObject outbreakResult = new JSONObject();
        JSONObject description = new JSONObject();
        description.put("name","Ebola");
        description.put("date","Octiber 18th, 2015");

        outbreakResult.put("description",description);

        JSONArray allSamples = new JSONArray();

        for (Map.Entry<String, Integer> entry : samples.entrySet())
        {
            //Add  metadata file to the samples
            JSONObject sample = new JSONObject();
            sample.put("name",entry.getKey());
            sample.put("date","");
            JSONArray symptoms = new JSONArray();
            sample.put("symptoms",symptoms);
            JSONArray geneUnits = new JSONArray();
            for (Map.Entry<String, Integer> entry2 : genes.entrySet()){
                JSONObject geneUnit =new JSONObject();
                geneUnit.put("name",entry2.getKey());
                geneUnit.put("abundance",entry2.getValue());
                geneUnits.add(geneUnit);
            }
            sample.put("GeneUnit",geneUnits);
            allSamples.add(sample);
        }
        outbreakResult.put("allSamples",allSamples);
        FileWriter outputFile = new FileWriter("/home/user/Desktop/IdeaProjects/WWH-BioApp_resources/Profiles/outputFile.json");
        outputFile.write(outbreakResult.toJSONString());
        outputFile.flush();
        outputFile.close();
        return outbreakResult.toString();
    }
}
