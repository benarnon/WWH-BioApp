/**
 * Created by user on 3/9/15.
 */
public class Bicluster {
    private float[][] cluster;
    private String[] samples;
    private String[] pathogens;
    private  float clusterScore;

    public Bicluster(int pathogenNum,int samplesNum) {
        cluster = new float[pathogenNum][samplesNum];
        samples = new String[samplesNum];
        pathogens = new String[pathogenNum];
    }

    public String[] getSamples() {
        return samples;
    }

    public void setSamples(String[] samples) {
        this.samples = samples;
    }

    public String[] getPathogens() {
        return pathogens;
    }

    public void setPathogens(String[] pathogens) {
        this.pathogens = pathogens;
    }

    public void addScore(int pathogenIndex,int sampleIndex, float score){
        cluster[pathogenIndex][sampleIndex] = score;
    }

    public float getScore(int pathogenIndex,int sampleIndex){
        return cluster[pathogenIndex][sampleIndex];
    }

    public void addSample(String sample, int index){
        samples[index] = sample;
    }

    public void addPathogen(String pathogen, int index){
        pathogens[index] = pathogen;
    }

    public String getSample(int index){
        return samples[index];
    }

    public String getPathogen(int index){
        return pathogens[index];
    }

    public void setClusterScore(){
        float best = 0;
        for (int i = 0; i < pathogens.length; i++) {
            float sum = 0;
            for (int j = 0; j < samples.length; j++)
                sum += cluster[i][j];

            float average = ((float) sum) / samples.length;
            if (Math.abs(average)>best)
                best = Math.abs(average);
        }
        clusterScore = best;
    }

    public float getClusterScore(){
        return clusterScore;
    }

    @Override
    public String toString() {
        String ans ="Cluster score:" + clusterScore +"\n";
        for (int i = 0; i < samples.length; i++) {
            ans = ans +"\t"+ samples[i];
        }
        ans= ans +"\n";
        for (int i = 0; i < pathogens.length; i++) {
            ans=  ans + pathogens[i];
            for (int j = 0; j < samples.length; j++) {
                ans =ans+ "\t" + cluster[i][j];
            }
            ans = ans + "\n";
        }
        ans = ans +"\n";

        return ans;
    }
}
