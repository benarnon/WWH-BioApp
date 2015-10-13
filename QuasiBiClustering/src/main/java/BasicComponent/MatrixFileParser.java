package BasicComponent;

import Enumeration.Hit;
import FeatureRelatedComponent.geneUnit;
import FeatureRelatedComponent.member;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 5/10/15.
 */
public class MatrixFileParser {
    private Hit[][] matrix;
    private ArrayList<member> geneUnits = new ArrayList<>();
    private ArrayList<Sample> samples = new ArrayList<>();


    public ArrayList<member> getGeneUnits() {
        return geneUnits;
    }

    public ArrayList<Sample> getSamples() {
        return samples;
    }

    public MatrixFileParser(File file, ArrayList<member> geneUnits, ArrayList<Sample> samples) throws IOException {
        matrix = new Hit[geneUnits.size()][samples.size()];
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine;
        if ( (nameLine = reader.readLine() ) != null){
            String[] SamplesName = nameLine.split("\t");
            for (int i = 0; i < SamplesName.length; i++) {
                this.samples.add(i, getSampleByName(samples, SamplesName[i]));
            }

        }
        int i =0;
        nameLine = reader.readLine();
        while(nameLine!=null){
            String[] split = nameLine.split("\t");
            String geneId = split[0];
            this.geneUnits.add(i,getGenomeByName(geneUnits,geneId));
            for (int k = 1; k < split.length; k++) {
                matrix[i][k-1] = new Hit(Float.parseFloat(split[k]));
            }
            i++;
            nameLine = reader.readLine();


        }

        System.out.println("Done parsing genes file");
        reader.close();
        fr.close();
        reader = null;
        fr = null;
        System.gc();

    }

    public Hit[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Hit[][] matrix) {
        this.matrix = matrix;
    }



    private Sample getSampleByName(ArrayList<Sample> samples,String name){
        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i).getSampleName().equals(name))
                return samples.get(i);
        }
        return null;
    }

    private geneUnit getGenomeByName(ArrayList<member> geneUnits, String name){
        for (int i = 0; i < geneUnits.size(); i++) {
            if (geneUnits.get(i).getName().equals(name))
                return (geneUnit)geneUnits.get(i);
        }
        return null;
    }

}
