package BasicComponent;

import Mains.EnumParams;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 5/10/15.
 */
public class SampleFileParser {
    ArrayList<Sample> samples = new ArrayList<>();
    int numOfSample = 0;

    public SampleFileParser(File file) throws Exception {
        parse_sample_file(file);
    }

    public ArrayList<Sample> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Sample> samples) {
        this.samples = samples;
    }

    public int getNumOfSample() {
        return numOfSample;
    }

    public void setNumOfSample(int numOfSample) {
        this.numOfSample = numOfSample;
    }

    private void parse_sample_file(File file) throws Exception {
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine;

        Sample sample;

        while((nameLine = reader.readLine()) !=null){
            numOfSample++;
            String SampleName ="";
            Date date = null;
            String[] split = nameLine.split("\\|");
            SampleName = split[0];
            int year = Integer.parseInt(split[1].split("/")[2]);
            int month = Integer.parseInt(split[1].split("/")[1]);
            int day = Integer.parseInt(split[1].split("/")[0]);
            date = new Date(year,month,day);
            String location =split[2];
            sample = new Sample(SampleName,date,location);
            sample.createNewFeature(EnumParams.GenesFeatureName);
            samples.add(sample);


        }

        System.out.println("Done parsing genes file");
        reader.close();
        fr.close();
        reader = null;
        fr = null;
        System.gc();
    }
}
