


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;




import java.io.*;
import java.util.Iterator;
import java.util.List;

public class VectorCreator {
    //Collecting all the contigs to one pathogen
    public static void CreateFinalVector(String vectorPath, List<String> DBlist, List<String> DBlength, String SampleName, String finalPath, int db) {
        //PathSum contain for each pathogen in the vector file the sum of all his contigs
        int[] PathSum = new int[DBlist.size()];

        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(vectorPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line = null;
            if (br != null) {
                line = br.readLine();
            }

            while (line != null) {
                String id = line.split("\t")[0];
                int length = Integer.parseInt(line.split("\t")[1]);
                //if line is of pathogen with no contigs

                if(!line.contains("|gb|")){
                    int in = DBlist.indexOf(id);
                    PathSum[in] = length;
                }
                //the pathogen is with contigs.
                else{
                    int indexCon = line.indexOf("|gb|");
                    //The 4 letters of the pathogen example: gi|158931597|gb|ABFK02000003.1| Alisti
                    String idCon = line.substring(indexCon+4,indexCon+8);
                    int iPatho = findPathogen(idCon,DBlist);
                    if(iPatho != -1){
                        PathSum[iPatho] += length;
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PrintWriter writer = null;
        try {
            if(db==0)
                writer = new PrintWriter(finalPath + SampleName +"-final.txt", "UTF-8");
            else if(db==1)
                writer = new PrintWriter(finalPath + SampleName +"-VectorEU.txt", "UTF-8");
            else if(db==2)
                writer = new PrintWriter(finalPath + SampleName +"-VectorEgypt.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            if(db ==1 )
                writer.println("EU/"+SampleName);
            if(db==2)
                writer.println("Egypt/"+SampleName);
        }
        String line = "";
        for (int i = 0; i < DBlist.size(); i++) {
            line = DBlist.get(i) + "\t" + 100*((float)PathSum[i]/Float.parseFloat(DBlength.get(i)));
            if (writer != null) {
                writer.println(line);
            }
        }

        if (writer != null) {
            writer.close();
        }
    }

    private static int findPathogen(String idCon, List<String> dBlist) {
        for (int i = 0; i < dBlist.size(); i++) {
            if (dBlist.get(i).contains("|gb|")){
                String tmp = dBlist.get(i);
                if (tmp.contains("|gb|" + idCon)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static class ClusterMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
        private final static IntWritable one = new IntWritable(1);
        private Text sampleID = new Text();







        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = (value.toString()).split("\t");
            sampleID.set(splits[0]);
            context.write(sampleID, one);
        }

    }


    public static class ClusterReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();



        public void reduce(Text key, Iterator<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            if (key.toString().compareTo(" ") == 1 | key.toString().compareTo("") == 1)
                return;
            result.set(sum);
            Text outKey = new Text();
            outKey.set(key.toString());
            outKey.set(outKey.toString());
            context.write(outKey, result);
        }
    }

}
