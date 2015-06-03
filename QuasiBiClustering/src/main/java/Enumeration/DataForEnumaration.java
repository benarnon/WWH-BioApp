package Enumeration;


import BasicComponent.*;
import FeatureRelatedComponent.FeatureArray;
import FeatureRelatedComponent.GeneUnit;
import FeatureRelatedComponent.Member;
import Mains.EnumParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



//This class will prepare the data for the enumaration process


public class DataForEnumaration
{
    public FeatureArray featureArray = new FeatureArray();
    public ArrayList<Sample> samples = new ArrayList<>();

    public Hit[][] matrix;

    public DataForEnumaration(String genomesFile, String sampleFile, String matrixFile) throws Exception {
        featureArray.createNewFeature(EnumParams.GenesFeatureName);
        featureArray.createNewFeature(EnumParams.SymptomsFeatureName);
        GenomeFileParser genome_parser = new GenomeFileParser(new File(genomesFile));
        featureArray.setFeatureMemberArray(EnumParams.GenesFeatureName, genome_parser.getGeneUnits());

        SampleFileParser sample_parser = new SampleFileParser(new File(sampleFile));
        samples = sample_parser.getSamples();

        MatrixFileParser matrix_parser = new MatrixFileParser(new File(matrixFile), featureArray.getFeature(EnumParams.GenesFeatureName).getFeatureMemebers(),samples);
        matrix = matrix_parser.getMatrix();

        parseMatrix(matrix_parser.getSamples(),matrix_parser.getGeneUnits());
        System.out.println("Finish prepare Data\n");


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
}
