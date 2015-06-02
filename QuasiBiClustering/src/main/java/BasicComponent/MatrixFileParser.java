package BasicComponent;

import Enumeration.Hit;
import FeatureRelatedComponent.GeneUnit;
import FeatureRelatedComponent.Member;

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

    public ArrayList<GeneUnit> getGeneUnits() {
        return geneUnits;
    }

    public void setGeneUnits(ArrayList<GeneUnit> geneUnits) {
        this.geneUnits = geneUnits;
    }

    public ArrayList<Sample> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Sample> samples) {
        this.samples = samples;
    }

    private ArrayList<GeneUnit> geneUnits = new ArrayList<>();
    private ArrayList<Sample> samples = new ArrayList<>();


    public MatrixFileParser(File file, ArrayList<Member> geneUnits, ArrayList<Sample> samples) throws IOException {

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine = reader.readLine();
        //Get the matrix dimensions

        if (nameLine != null){
            String[] split = nameLine.split(" ");
            matrix = new Hit[Integer.parseInt(split[0])][Integer.parseInt(split[1])];

        }
        //Get all the samples
        nameLine = reader.readLine();
        if (nameLine != null){
            String[] SamplesName = nameLine.split("\t");
            for (int i = 0; i < SamplesName.length; i++) {
                this.samples.add(i, getSampleByName(samples, SamplesName[i]));
            }

        }
        int i =0;
        nameLine = reader.readLine();
        while(nameLine!=null){
            String[] split = nameLine.split("\t");
            int l = split[0].split("\\|").length;
            this.geneUnits.add(i, getGenomeByName(geneUnits,split[0].split("\\|")[l-1].substring(1)));
            for (int k = 2; k < split.length; k++) {
                matrix[i][k-2] = new Hit(Float.parseFloat(split[k]));
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

    private GeneUnit getGenomeByName(ArrayList<GeneUnit> geneUnits, String name){
        for (int i = 0; i < geneUnits.size(); i++) {
            if (geneUnits.get(i).getGenomeName().equals(name))
                return geneUnits.get(i);
        }
        return null;
    }

}
