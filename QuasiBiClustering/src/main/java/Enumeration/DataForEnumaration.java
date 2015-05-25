package Enumeration;


import BasicComponent.*;
import Mains.EnumParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



//This class will prepare the data for the enumaration process


public class DataForEnumaration
{
    public ArrayList<GeneUnit> geneUnitList = new ArrayList<>();
    public ArrayList<Sample> samples = new ArrayList<>();

    public Hit[][] matrix;

    public DataForEnumaration(String genomesFile, String sampleFile, String matrixFile) throws IOException {
        GenomeFileParser genome_parser = new GenomeFileParser(new File(genomesFile));
        geneUnitList = genome_parser.getGeneUnits();

        SampleFileParser sample_parser = new SampleFileParser(new File(sampleFile));
        samples = sample_parser.getSamples();

        MatrixFileParser matrix_parser = new MatrixFileParser(new File(matrixFile), geneUnitList,samples);
        matrix = matrix_parser.getMatrix();

        parseMatrix(matrix_parser.getSamples(),matrix_parser.getGeneUnits());



    }

    private void parseMatrix(ArrayList<Sample> samples, ArrayList<GeneUnit> geneUnits) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j].getScore()==1){
                    int sampleIndex = samples.indexOf(samples.get(j));
                    this.samples.get(sampleIndex).addGenome(geneUnits.get(i));

                }
            }
        }
    }

    public ArrayList<GeneUnit> getGeneUnitList() {
        return geneUnitList;
    }

    public void setGeneUnitList(ArrayList<GeneUnit> geneUnitList) {
        this.geneUnitList = geneUnitList;
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
