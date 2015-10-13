package BasicComponent;

import FeatureRelatedComponent.geneUnit;
import FeatureRelatedComponent.member;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by user on 5/10/15.
 */
public class GenomeFileParser {
    private ArrayList<member> geneUnits = new ArrayList<>();
    private int numOfGenomes;
    public GenomeFileParser(File file) throws IOException {
        parse_genome_file(file);
    }

    private void parse_genome_file(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine;

        numOfGenomes =0;
        geneUnit geneUnit;

        while((nameLine = reader.readLine())!=null){
            numOfGenomes++;
            String name = nameLine.split("\t")[0];
            int length = Integer.parseInt(nameLine.split("\t")[1]);
            geneUnit = new geneUnit(name,length);
            geneUnits.add(geneUnit);
        }

        System.out.println("Done parsing genes file");
        reader.close();
        fr.close();
        reader = null;
        fr = null;
        System.gc();

    }

    public ArrayList<member> getGeneUnits() {
        return geneUnits;
    }
}
