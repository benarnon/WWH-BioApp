/**
 * Created by user on 3/4/15.
 */
public class PathogenScores {
    private String name;
    private float[] ScoreArray;

    public PathogenScores(String name, float[] scoreArray) {
        this.name = name;

        ScoreArray = scoreArray;
    }

    public PathogenScores(int numOfSamples){
        ScoreArray = new float[numOfSamples];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }


    public float[] getScoreArray() {
        return ScoreArray;
    }

    public void setScoreArray(float[] scoreArray) {
        ScoreArray = scoreArray;
    }

    public void addScore(float score, int index){
        ScoreArray[index] = score;
    }

    public float getScore(int index) { return ScoreArray[index]; }
}
