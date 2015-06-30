package Enumeration;


import BasicComponent.*;
import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import FeatureRelatedComponent.Member;
import FeatureRelatedComponent.Symptom;
import Mains.EnumParams;

import java.io.*;
import java.util.ArrayList;



//This class will prepare the data for the enumaration process


public class DataForEnumaration
{
    public FeatureArray featureArray = new FeatureArray();
    public ArrayList<Sample> samples = new ArrayList<>();

    public Hit[][] matrix;
    //Input for the constructor: Array of Feature files (GeneUnit file,Symptoms file etc..), Samples file, Matrix file.
    public DataForEnumaration(ArrayList<String> featureFiles,String sampleFile, String matrixFile) throws Exception {
        //Create the features will be dynamic process in the future
        featureArray.createNewFeature(EnumParams.GenesFeatureName);
        featureArray.createNewFeature(EnumParams.SymptomsFeatureName);
        //Get the first feature GeneUnitFeature from the
        GenomeFileParser genome_parser = new GenomeFileParser(new File(featureFiles.get(0)));
        featureArray.setFeatureMemberArray(EnumParams.GenesFeatureName, genome_parser.getGeneUnits());

        //Get the second feature
        featureArray.setFeatureMemberArray(EnumParams.SymptomsFeatureName,parseSymptomsFile(featureFiles.get(1)));

        SampleFileParser sample_parser = new SampleFileParser(new File(sampleFile));
        samples = sample_parser.getSamples();

        //Get the other features.
        MatrixFileParser matrix_parser = new MatrixFileParser(new File(matrixFile), featureArray.getFeature(EnumParams.GenesFeatureName).getFeatureMemebers(),samples);
        matrix = matrix_parser.getMatrix();

        parseMatrix(matrix_parser.getSamples(),matrix_parser.getGeneUnits());
        System.out.println("Finish prepare Data\n");


    }

    private ArrayList<Member> parseSymptomsFile(String symptomsFile) throws IOException {
        ArrayList<Member> ans = new ArrayList<>();
        FileReader fr = new FileReader(symptomsFile);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine = reader.readLine();

        int numOfSymptoms = 0;
        Symptom Symptom;

        while (nameLine != null) {
            numOfSymptoms++;
            nameLine = nameLine.substring(0);
            String[] split = nameLine.split("\t");
            Symptom = new Symptom(nameLine);
            ans.add(Symptom);
            nameLine = reader.readLine();



        }
        return ans;
    }

    private void parseMatrix(ArrayList<Sample> samples, ArrayList<Member> geneUnits) throws Exception {
        for (int i = 0; i < matrix.length; i++) {//for each geneUnit
            for (int j = 0; j < matrix[0].length; j++) {//for each sample
                if (matrix[i][j].getScore()==1){

                    int sampleIndex = samples.indexOf(samples.get(j));
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

    public FeatureArray getFeatureArray() {
        return featureArray;
    }
}
