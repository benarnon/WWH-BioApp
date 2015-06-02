package BasicComponent;

import FeatureRelatedComponent.Symptom;
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
        String nameLine = reader.readLine();

        Sample sample;

        while(nameLine!=null){
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
            String[] symptoms = split[3].split(",");


            sample = new Sample(SampleName,date,location);

            //Create the first feature Symptoms
            sample.createNewFeature(EnumParams.GenesFeatureName);
            sample.createNewFeature(EnumParams.SymptomsFeatureName);
            for (int i = 0; i < symptoms.length; i++) {
                Symptom sym = new Symptom(symptoms[i]);
                sample.setMember(EnumParams.SymptomsFeatureName,sym);
            }

            samples.add(sample);
            nameLine = reader.readLine();


        }

        System.out.println("Done parsing genes file");
        reader.close();
        fr.close();
        reader = null;
        fr = null;
        System.gc();
    }
}
