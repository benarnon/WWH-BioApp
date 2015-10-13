package matrixCreator;

/**
 * Created by user on 10/11/15.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class MatrixCreator {
    private static final Log LOG = LogFactory.getLog(MatrixCreator.class);


    public static class ClusterMapper extends Mapper <LongWritable, Text, Text, Text>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
                if (value.toString().split("\t").length > 4) {
                    if (value.toString().split("\t")[3].equals("+") || value.toString().split("\t")[3].equals("-")) {
                        if (value.toString().split("\t")[0].split("#").length == 3) {
                            String sampleName = value.toString().split("\t")[0].split("#")[0];
                            String clusterId = value.toString().split("\t")[0].split("#")[1];
                            String geneUnit = value.toString().split("\t")[0].split("#")[2];
                            String depth = value.toString().split("\t")[4];
                            //context.write(new Text(sampleName), new Text(clusterId + "{GENE: " + geneUnit + ", DEPTH: " + depth + " }\n"));
                            context.write(new Text(sampleName + "\n"), new Text(geneUnit + "\t" + depth + " \n"));
                        }
                    }
                }
        }
    }

    public static class ClusterReducer extends Reducer <Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String depthList="";
            Iterator valIter = values.iterator();
            while(valIter.hasNext()){
                depthList = depthList + valIter.next();
            }
            context.write(key,new Text(depthList));
        }
    }
}
