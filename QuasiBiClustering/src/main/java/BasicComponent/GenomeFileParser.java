package BasicComponent;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by user on 5/10/15.
 */
public class GenomeFileParser {
    private ArrayList<GeneUnit> geneUnits = new ArrayList<>();
    private int numOfGenomes;
    public GenomeFileParser(File file) throws IOException {
        parse_genome_file(file);
    }

    private void parse_genome_file(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String nameLine = reader.readLine();

        numOfGenomes =0;
        GeneUnit geneUnit;

        while(nameLine!=null){
            numOfGenomes++;
            nameLine = nameLine.substring(1);
            String[] split = nameLine.split("\t");
            String stmp = split[0];
            String [] split_tmp = stmp.split("\\|");
            DatabaseIndex [] DbStrings = new DatabaseIndex[(split_tmp.length-1)/2];
            int i =0;
            int j= 0;
            while (i<split_tmp.length-2){
               DbStrings[j] = new DatabaseIndex(split_tmp[i],split_tmp[i+1]);
                i = i+2;
                j++;
            }

            String name = split_tmp[split_tmp.length-1].substring(1);

            geneUnit = new GeneUnit(name,Integer.parseInt(split[1]),DbStrings);
            geneUnits.add(geneUnit);
            nameLine = reader.readLine();


        }

        System.out.println("Done parsing genes file");
        reader.close();
        fr.close();
        reader = null;
        fr = null;
        System.gc();

    }

    public ArrayList<GeneUnit> getGeneUnits() {
        return geneUnits;
    }
}
