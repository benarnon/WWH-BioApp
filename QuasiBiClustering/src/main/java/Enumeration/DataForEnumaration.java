package Enumeration;


import BasicComponent.*;
import FeatureRelatedComponent.featureArray;
import FeatureRelatedComponent.member;
import Mains.EnumParams;

import java.io.*;
import java.util.ArrayList;



//This class will prepare the data for the enumaration process


public class DataForEnumaration
{
    public FeatureRelatedComponent.featureArray featureArray = new featureArray();
    public ArrayList<Sample> samples = new ArrayList<>();

    public Hit[][] matrix;
    //Input for the constructor: Array of Feature files (GeneUnit file,Symptoms file etc..), Samples file, Matrix file.
    public DataForEnumaration(ArrayList<String> featureFiles,String sampleFile, String matrixFile) throws Exception {
        //Create the features will be dynamic process in the future
        featureArray.createNewFeature(EnumParams.GenesFeatureName);

        //If we add another features
        //featureArray.createNewFeature(EnumParams.SymptomsFeatureName);
        //Get the first feature GeneUnitFeature from the
        GenomeFileParser genome_parser = new GenomeFileParser(new File(featureFiles.get(0)));
        featureArray.setFeatureMemberArray(EnumParams.GenesFeatureName, genome_parser.getGeneUnits());

        //Get the second feature
        //featureArray.setFeatureMemberArray(EnumParams.SymptomsFeatureName,parseSymptomsFile(featureFiles.get(1)));

        SampleFileParser sample_parser = new SampleFileParser(new File(sampleFile));
        samples = sample_parser.getSamples();

        //Get the other features.
        MatrixFileParser matrix_parser = new MatrixFileParser(new File(matrixFile), featureArray.getFeature(EnumParams.GenesFeatureName).getFeatureMemebers(),samples);
        matrix = matrix_parser.getMatrix();

        parseMatrix(matrix_parser.getSamples(),matrix_parser.getGeneUnits());
        System.out.println("Finish prepare Data\n");


    }

    private ArrayList<member> parseSymptomsFile(String symptomsFile) throws IOException {
        ArrayList<member> ans = new ArrayList<>();
        FileReader fr = new FileReader(symptomsFile);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine = reader.readLine();

        int numOfSymptoms = 0;

        while (nameLine != null) {
            numOfSymptoms++;
            nameLine = nameLine.substring(0);
            String[] split = nameLine.split("\t");
            nameLine = reader.readLine();



        }
        return ans;
    }

    private void parseMatrix(ArrayList<Sample> samples, ArrayList<member> geneUnits) throws Exception {
        this.samples = samples;
        /*for (int i = 1; i < this.samples.size(); i++) {
            this.samples.get(i).createNewFeature(EnumParams.GenesFeatureName);
        }*/
        for (int i = 0; i < matrix.length; i++) {//for each geneUnit
            for (int j = 0; j < matrix[0].length; j++) {//for each sample
                if (matrix[i][j].getScore()==1){

                    int sampleIndex = samples.indexOf(samples.get(j+1));
                    String sampleId = samples.get(j+1).getSampleName();
                    this.samples.get(sampleIndex).setMember(EnumParams.GenesFeatureName, geneUnits.get(i));

                }
            }
        }
    }

    public ArrayList<Sample> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Sample> samples) {
        this.samples = samples;
    }

    public Hit[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Hit[][] matrix) {
        this.matrix = matrix;
    }

    public FeatureRelatedComponent.featureArray getFeatureArray() {
        return featureArray;
    }
}
