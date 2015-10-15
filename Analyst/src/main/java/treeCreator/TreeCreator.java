package treeCreator;//package treeCreator;

import org.json.simple.JSONArray;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import treeCreator.Vertex;

import java.awt.Color;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gal on 9/1/2015.
 */
public class TreeCreator {

    private ArrayList<Vertex> vertexList;
    // private Rengine rengine;

    public TreeCreator(){
        vertexList = new ArrayList<Vertex>();
        //rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        // System.out.println("Rengine created, waiting for R");
    }


    //main function:
    // 1) initializing vertex list containing the abundance vectors.
    // 2) calculating the weights for the edges.
    // 3) run "seqtrak" to obtain MST.
    // 4) write a .json file containing the tree.
    // 5) cleanup

    public void CreateTree(String inputFileLocation, String midFilesFolderLocation, String outPutFilesFolderLocation, String rScriptLocation, String id){
        parseInputJSON(inputFileLocation, id);
        double[][] spearmanMatrix = createEdges(vertexList.size());
        String[] dates = createDatesArray();
        makeCSVFiles(spearmanMatrix, dates, midFilesFolderLocation);
        Process callSeqTrak = null;
        try {
            //calling r script to run seqTrak, an wait for the script
            //to finish before proceeding
            callSeqTrak = Runtime.getRuntime().exec("Rscript " + rScriptLocation + " \"" + midFilesFolderLocation + "\"");
            callSeqTrak.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buildOutputJson(midFilesFolderLocation, outPutFilesFolderLocation, id);
        cleanFiles(midFilesFolderLocation);

    }

    // parsing the .json input file to Vertexes
    // saved in vertexList
    public void parseInputJSON(String location, String id){
        JSONParser parser = new JSONParser();
        try {

            //for the id's
            int counter = 1;
            //formal thing
            Object obj = parser.parse(new FileReader(location));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray samples = (JSONArray) jsonObject.get("samples");//allSamples
            Iterator<JSONObject> iter = samples.iterator();
            //iterate over samples
            while(iter.hasNext()){
                JSONObject currSample = iter.next();
                Vertex currVertex = new Vertex(((String) currSample.get("date")),counter, ((String) currSample.get("name")), ((double) currSample.get("longitude")),  ((double) currSample.get("latitude")));
                //Vertex currVertex = new Vertex(((String) currSample.get("date")),counter, ((String) currSample.get("name")));
                //iterate over symptoms
               /* JSONArray symptoms = (JSONArray) currSample.get("symptoms");
                Iterator<String> symptomIterator = symptoms.iterator();
                while(symptomIterator.hasNext()){
                    String currSymptom = symptomIterator.next();
                    currVertex.addSymptom(currSymptom);
                }*/
                //iterate over gene abundances
                JSONArray abundances = (JSONArray) currSample.get("gene-units");
                Iterator<JSONObject> geneIter = abundances.iterator();
                while(geneIter.hasNext()) {
                    JSONObject currGene = geneIter.next();
                    currVertex.AddAbundance(((Number) (currGene.get("abundance"))).doubleValue(), ((String) (currGene.get("id"))));
                }
                currVertex.setRanks();
                vertexList.add(currVertex);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // calculate "all against all" spearman correlation coefficient
    // as input to "seqTrak"
    public double[][] createEdges(int n){
        double[][] spearmanMatrix = new double[n][n];
        double weight;
        for(int i=0; i < n-1; i++){
            for(int j=i+1; j < n; j++){
                weight = calculateSpearmanCorrelation(vertexList.get(i), vertexList.get(j));
                spearmanMatrix[i][j] = weight;
                spearmanMatrix[j][i] = weight;
            }
        }
        return spearmanMatrix;
    }

    // create dates array needed for seqTrak processing
    private String[] createDatesArray() {
        String[] dates = new String[vertexList.size()];
        for(int i = 0; i < vertexList.size(); i++){
            dates[i] = vertexList.get(i).getDate();
        }
        return dates;
    }

    // creating matrix.csv and dates.csv files needed
    // for seqTrak processing
    private void makeCSVFiles(double[][] spearmanMatrix, String[] datesArray, String midFilesFolderLocation) {
        try {
            //create matrix csv file
            File matrix = new File(midFilesFolderLocation+"/matrix.csv");
            matrix.createNewFile();
            FileWriter matrixOutput = new FileWriter(matrix);
            matrixOutput.flush();
            for(int i = 0; i < spearmanMatrix.length; i++){
                for(int j = 0; j < spearmanMatrix[0].length; j++){
                    matrixOutput.append("" + spearmanMatrix[i][j]);
                    if(j < spearmanMatrix[0].length-1){
                        matrixOutput.append(",");
                    }
                }
                if(i < spearmanMatrix.length-1){
                    matrixOutput.append("\n");
                }
                matrixOutput.flush();
            }
            matrixOutput.close();

            //create dates csv file
            File dates = new File(midFilesFolderLocation+"/dates.csv");
            dates.createNewFile();

            FileWriter datesOutput = new FileWriter(dates);
            datesOutput.append("id,collec.dates");
            datesOutput.append("\n");
            for(int i = 0; i < datesArray.length ; i++ ){
                datesOutput.append(i+1 + "," +datesArray[i] + " ");
                if(i < datesArray.length-1){
                    datesOutput.append("\n");
                }
            }
            datesOutput.flush();
            datesOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // generate .json file from seqTrak's results
    private void buildOutputJson(String midFilesFolderLocation, String outputFilesFolderLocation, String id) {
        String line;
        String splitBy = ",";

        JSONObject json = new JSONObject();
        json.put("type","FeatureCollection");
        json.put("created","<some_date>");
        json.put("announced_date","<some_date>");

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        // Creating new json object - 'properties'
        JSONObject properties = new JSONObject();
        properties.put("name", "<some_name>");
        properties.put("analized_data", "<some_date>");

        // Creating new json object - 'geometry'
        JSONObject geometry = new JSONObject();
        geometry.put("type", "GeometryCollection");

        // Generate 'geometries' jsonArray, including all geometries(Points and Lines)
        JSONArray geometries_list = new JSONArray();

        for(Vertex currVertex : vertexList) {
            // Generate 'geometries' jsonArray
            JSONObject geometry_obj = new JSONObject();
            geometry_obj.put("type", "Point");

            // Inject for each geometry object(i.e. json object) inside geometries array(i.e. json array) properties object(i.e. json object)
            JSONObject inside_properties = new JSONObject();
            inside_properties.put("name",currVertex.getName());

            // Inside 'properties', inject 'symptoms' object(i.e. json object)
            JSONArray symptoms = new JSONArray();

            for(String currSymptom : currVertex.getSymptomsList()){
                symptoms.add(currSymptom);
            }

            inside_properties.put("symptoms", symptoms);
            geometry_obj.put("properties", inside_properties);

            // For each 'point', generate coordinates object(i.e. json object)
            JSONArray coordinates = new JSONArray();
            coordinates.add(currVertex.getLongitude());
            coordinates.add(currVertex.getLatitude());
            geometry_obj.put("coordinates", coordinates);

            geometries_list.add(geometry_obj);

        }

        // Generate edges
        try {
            BufferedReader br = new BufferedReader(new FileReader(midFilesFolderLocation+"/res.csv"));
            String[][] seqTrakResaults = new String[vertexList.size()][6];
            // Dump first line
            line = br.readLine();
            int j = 0;
            // Each line in "res.csv" is an edge
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] parsedLine = line.split(splitBy);
                for(int i = 0; i < parsedLine.length; i++){
                    seqTrakResaults[j][i] = parsedLine[i];
                }
                j++;
            }

            br.close();
            // Create edges
            for(int i = 0; i < seqTrakResaults.length; i++){
                if(!seqTrakResaults[i][2].equals(new String("NA"))){

                    // Generate 'geometries' jsonArray
                    JSONObject geometry_obj = new JSONObject();
                    geometry_obj.put("type", "LineString");

                    // Inject for each geometry object(i.e. json object) inside geometries array(i.e. json array) properties object(i.e. json object)
                    JSONObject inside_properties = new JSONObject();
                    inside_properties.put("name", "<LineString_name>");

                    JSONArray coordinates = new JSONArray();

                    JSONArray source =  new JSONArray();

                    String ancestor = seqTrakResaults[new Integer(seqTrakResaults[i][2]).intValue() - 1][0];
                    Vertex sorceVertex = vertexList.get(new Integer(ancestor.substring(1,ancestor.length()-1))-1);
                    source.add(sorceVertex.getLongitude());
                    source.add(sorceVertex.getLatitude());
                    coordinates.add(source);

                    JSONArray destination = new JSONArray();
                    String dest = new String(seqTrakResaults[i][0]);
                    Vertex destinationVertex = vertexList.get(new Integer(dest.substring(1,dest.length()-1)).intValue()-1);
                    destination.add(destinationVertex.getLongitude());
                    destination.add(destinationVertex.getLatitude());
                    coordinates.add(destination);

                    geometry_obj.put("coordinates", coordinates);
                    geometries_list.add(geometry_obj);

                }
            }

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        geometry.put("geometries", geometries_list);

        feature.put("geometry", geometry);
        // Add 'properties' jsonObject to 'json' jsonObject
        feature.put("properties", properties);

        JSONArray features_list = new JSONArray();
        features_list.add(feature);
        json.put("features", features_list);

        try {
            FileWriter outputFile = new FileWriter(outputFilesFolderLocation+"/map_" + id + ".geojson");
            outputFile.write(json.toJSONString());
            outputFile.flush();
            outputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // clean all files on midFilesFolder
    private void cleanFiles(String midFilesFolderLocation) {
        try {
            //Files.delete(Paths.get(midFilesFolderLocation+"\\matrix.csv"));
            Files.delete(Paths.get(midFilesFolderLocation+"/dates.csv"));
            //Files.delete(Paths.get(midFilesFolderLocation+"\\res.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // for future use, if we can work with JRI
    private void CreateTransmissionTree(double[][] spearmanMatrix, String[] dates) {


    }

    // aux function
    private void printDates(String[] dates) {
        StringBuilder str = new StringBuilder();
        str.append("[");
        for(String i : dates){
            str.append(i + ",");
        }
        str.append("]");
        System.out.println(str);
    }



    //aux function
    private void printMatrix(String[][] matrix){
        for(int i=0; i < matrix.length; i++){
            for(int j=0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }



    // calculates spearman correlation as explained in the literature
    // return 1-p (p being spearman's correlation)
    // because we want MST
    public double calculateSpearmanCorrelation(Vertex source, Vertex destination) {
        double numOfGenes = source.getListSize();
        double sum = 0;
        double row;
        for(int i = 0; i < numOfGenes; i++){
            //System.out.println(source.getName() +" " +destination.getAbundancesList());
            sum += Math.pow(source.getRankAt(i)-destination.getRankAt(i),2);
        }
        row = 1 - ((6*sum)/(numOfGenes * ((Math.pow(numOfGenes,2)-1))));
        return 1 - row;
    }


    public double[][] CreateAbundanceMatrix(String location){
        JSONParser parser = new JSONParser();
        double[][] abundanceMatrix = null;
        try {

            //for the id's
            int num_of_samples = 0;
            int num_of_genes = 0;
            boolean checkIn = false;

            //formal thing
            Object obj = parser.parse(new FileReader(location));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray samples = (JSONArray) jsonObject.get("allSamples");

            Iterator<JSONObject> iter = samples.iterator();
            //iterate over samples
            while(iter.hasNext()){
                JSONObject currSample = iter.next();


                if(!checkIn){
                    //iterate over gene abundances
                    JSONArray abundances = (JSONArray) currSample.get("GeneUnit");
                    Iterator<JSONObject> geneIter = abundances.iterator();
                    while(geneIter.hasNext()) {
                        JSONObject currGene = geneIter.next();
                        num_of_genes++;
                    }

                    checkIn = true;
                }

                num_of_samples++;
            }

            abundanceMatrix = new double[num_of_genes][num_of_samples];
            num_of_genes = 0;
            num_of_samples = 0;


            iter = samples.iterator();
            //iterate over samples
            while(iter.hasNext()){
                JSONObject currSample = iter.next();

                //iterate over gene abundances
                JSONArray abundances = (JSONArray) currSample.get("GeneUnit");
                Iterator<JSONObject> geneIter = abundances.iterator();
                while(geneIter.hasNext()) {
                    JSONObject currGene = geneIter.next();
                    abundanceMatrix[num_of_genes][num_of_samples] = ((Number) currGene.get("abundance")).doubleValue();
                    num_of_genes++;
                }

                num_of_genes = 0;
                num_of_samples++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return abundanceMatrix;
    }
    /*
    public void createBiHeatMaps(String location){

        JSONParser parser = new JSONParser();
        double[][] abundanceMatrix = null;

        int bi = 0;


        Object obj = null;
        try {
            obj = parser.parse(new FileReader(location));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray clusters = (JSONArray) jsonObject.get("BiClusters");

        Iterator<JSONObject> iter = clusters.iterator();

        while(iter.hasNext()){

            JSONObject currCluster = iter.next();
            JSONArray samples = (JSONArray) currCluster.get("allSamples");
            Iterator<JSONObject> samplesIter = samples.iterator();

            boolean checkIn = false;
            int num_of_genes = 0;
            int num_of_samples = 0;


            //iterate over samples
            while(samplesIter.hasNext()){
                JSONObject currSample = samplesIter.next();


                if(!checkIn){
                    //iterate over gene abundances
                    JSONArray abundances = (JSONArray) currSample.get("GeneUnit");
                    Iterator<JSONObject> geneIter = abundances.iterator();
                    while(geneIter.hasNext()) {
                        JSONObject currGene = geneIter.next();
                        num_of_genes++;
                    }

                    checkIn = true;
                }

                num_of_samples++;
            }

            abundanceMatrix = new double[num_of_genes][num_of_samples];
            num_of_genes = 0;
            num_of_samples = 0;




            samplesIter = samples.iterator();
            //iterate over samples
            while(samplesIter.hasNext()){
                JSONObject currSample = samplesIter.next();
//                num_of_samples = 0;                

                //iterate over gene abundances
                JSONArray abundances = (JSONArray) currSample.get("GeneUnit");
                Iterator<JSONObject> geneIter = abundances.iterator();
                while(geneIter.hasNext()) {
                    JSONObject currGene = geneIter.next();
                    abundanceMatrix[num_of_genes][num_of_samples] = ((Number) currGene.get("abundance")).doubleValue();
                    num_of_genes++;
                }

                num_of_genes = 0;
                num_of_samples++;

            }

            // Step 1: Create our heat map chart using our data.
            HeatChart map = new HeatChart(abundanceMatrix);

            // Step 2: Customise the chart.
            map.setTitle("");
            map.setXAxisLabel("Samples");
            map.setYAxisLabel("Genomes");
            map.setHighValueColour(new Color(65280));
            map.setLowValueColour(new Color(16136215));

            try {
                map.saveToFile(new File("./WWH-results/app/views/heat_maps/bi_heatmap_" + bi  + ".png"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            bi++;

            // abundanceMatrix;


        }






    }
    */

    public int numOfBiClusters(String location) throws FileNotFoundException, IOException, ParseException{
        JSONParser parser = new JSONParser();


        //for the id's
        int num_of_bi = 0;

        //formal thing
        Object obj = parser.parse(new FileReader(location));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray samples = (JSONArray) jsonObject.get("BiClusters");

        Iterator<JSONObject> iter = samples.iterator();

        while(iter.hasNext()){

            JSONObject currSample = iter.next();
            num_of_bi++;
        }




        return num_of_bi;









    }


}
