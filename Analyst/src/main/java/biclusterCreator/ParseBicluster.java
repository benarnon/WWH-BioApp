package biclusterCreator;

import matrixCreator.Matrix;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * method parseBicluster
 * input: biclutserFilePath,samplesFilePath,outputDirPath
 * it creates Json file for each bicluster in biclutserFilePath.
 */
public class ParseBicluster {
    List<Bicluster>  bicluterList = new ArrayList<Bicluster>();
    private Map<String,SampleJson> samplesFromJson = new HashMap<>();

    public ParseBicluster() {
    }

    public void parseBicluster(String biclutserFilePath, String samplesFilePath, String outputDirPath) throws IOException, ParseException {
        parseSamplesFiles(samplesFilePath);
        parseBiclutserFile(biclutserFilePath);
        biclutersToJson(outputDirPath);

    }

    public void parseBiclutserFile(String biclutserFilePath) throws IOException {
        Path pt=new Path(biclutserFilePath);
        FileSystem fs = FileSystem.get(new Configuration());
        if (!fs.exists(pt))
            System.out.println("Input file not found");
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        String line;
        String currentBicluster;
        int geneIndex;
        line = br.readLine();
        while(line  != null ) {
            currentBicluster = line.split(" ")[0];
            double score = Double.parseDouble(line.split(" ")[1]);
            int rows = Integer.parseInt(line.split(" ")[2]);
            int cols = Integer.parseInt(line.split(" ")[3]);
            Bicluster bicluster = new Bicluster(currentBicluster,score,rows,cols);
            line = br.readLine();
            String[] split = line.split("\t");
            //Fill the samples list
            for (int i = 0; i < split.length; i++) {
                bicluster.addSample(split[i],i);
            }
            line = br.readLine();
            geneIndex = 0;
            //Fill the gene list and the abundance matrix
            while(line != null && line.split("\t").length > 1){
                String[] split2 = line.split("\t");
                bicluster.addGene(split2[0],geneIndex);
                for (int i = 1; i < split2.length; i++) {
                    bicluster.setAbundance(geneIndex,i-1,Double.parseDouble(split2[i]));
                }
                line = br.readLine();
                geneIndex++;
            }
            bicluterList.add(bicluster);
        }
    }

    public void parseSamplesFiles(String samplesFilePath) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(samplesFilePath));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray samples = (JSONArray) jsonObject.get("samples");
        Iterator<JSONObject> iter = samples.iterator();
        //iterate over samples
        while(iter.hasNext()) {
            JSONObject sample = iter.next();
            String date = (String)sample.get("date");
            String name = (String )sample.get("id");
            Double latitude = (Double)sample.get("Latitude");
            Double longitude = (Double)sample.get("Longitude");
            samplesFromJson.put(name,new SampleJson(date,latitude,longitude));

        }


    }
    //TODO continue from here create the json files
    private void biclutersToJson(String outputDirPath) throws IOException {
        //outbreak test results
        JSONObject allBiclusters = new JSONObject();
        JSONArray biclusters = new JSONArray();
        outputDirPath = "/home/user/IdeaProjects/WWH-BioApp_resources/WWH-results/app/biclusters/";
        for (int i = 0; i < bicluterList.size(); i++) {
            Bicluster bic = bicluterList.get(i);
            JSONObject bicluster = new JSONObject();
            JSONObject description = new JSONObject();
            description.put("id",i);
            description.put("score",bic.getScore());
            bicluster.put("description",description);
            JSONArray samples = new JSONArray();
            for (int j = 0; j < bic.getNumOfSamples(); j++) {
                JSONObject sample = new JSONObject();
                //TODO get on sample from sample file
                sample.put("name", bic.getSample(j).substring(1,bic.getSample(j).length()-1).split("\\.")[1]);
                sample.put("date",samplesFromJson.get(bic.getSample(j).substring(1,bic.getSample(j).length()-1).split("\\.")[1]).getDate());
                sample.put("latitude",samplesFromJson.get(bic.getSample(j).substring(1,bic.getSample(j).length()-1).split("\\.")[1]).getLatitude());
                sample.put("longitude",samplesFromJson.get(bic.getSample(j).substring(1,bic.getSample(j).length()-1).split("\\.")[1]).getLongitude());
                //sample.put("symptoms..)
                //...
                JSONArray geneunits = new JSONArray();
                for (int k = 0; k < bic.getNumOfGenes(); k++) {
                    JSONObject gene = new JSONObject();
                    gene.put("id",bic.getGene(k).substring(1,bic.getGene(k).length()-1));
                    gene.put("abundance",bic.getAbundance(k,j));
                    geneunits.add(k,gene);
                }
                sample.put("gene-units",geneunits);
                samples.add(j,sample);
            }
            bicluster.put("samples",samples);
            FileWriter outputFile = new FileWriter(outputDirPath + "/bicluster" + i + ".json");
            outputFile.write(bicluster.toJSONString());
            outputFile.flush();
            outputFile.close();
            System.out.println("Finish to write Bicluster file " + outputDirPath + "/Bicluster-" + i + ".json");

            biclusters.add(bicluster);

            JSONObject runJson = new JSONObject();
            JSONArray descriptionForRun = new JSONArray();
            JSONObject subDescription = new JSONObject();
            subDescription.put("name","Ebola");
            subDescription.put("date","2012-10-15");
            descriptionForRun.add(subDescription);
            runJson.put("description",descriptionForRun);
            runJson.put("files",3);

            outputFile = new FileWriter(outputDirPath + "/RunDescription.json");
            outputFile.write(runJson.toJSONString());
            outputFile.flush();
            outputFile.close();
            System.out.println(outputDirPath + "/RunDescription.json");
        }
        allBiclusters.put("BiClusters",biclusters);
        FileWriter outputFile = new FileWriter(outputDirPath + "/all.json");
        outputFile.write(allBiclusters.toJSONString());
        outputFile.flush();
        outputFile.close();
    }
}
