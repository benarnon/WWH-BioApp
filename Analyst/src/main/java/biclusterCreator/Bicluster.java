package biclusterCreator;

import matrixCreator.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 10/13/15.
 */
public class Bicluster {
    private double[][] matrix;
    private List<String> samples = new ArrayList<String>();
    private List<String> genes = new ArrayList<String>() ;
    private double score;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bicluster(String id, double score, int rows, int cols){
        this.id = id;
        this.score = score;
        matrix = new double[rows][cols];
    }

    public Double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addSample(String sample, int index){
        samples.add(index,sample);
    }

    public void addGene(String gene, int index){
        genes.add(index,gene);
    }

    public String getSample(int index){
        return samples.get(index);
    }

    public String getGene(int index){
        return genes.get(index);
    }

    public void setAbundance(int indexGene,int indexSample, double abundnace){
        matrix[indexGene][indexSample] = abundnace;
    }

    public double getAbundance(int indexGene,int indexSample){
        return matrix[indexGene][indexSample];
    }

    public int getNumOfSamples(){
        return samples.size();
    }

    public int getNumOfGenes(){
        return genes.size();
    }


}
