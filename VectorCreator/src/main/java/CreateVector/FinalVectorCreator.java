package CreateVector;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.LongWritable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/2/15.
 * VecorUnit class will be replaced by the global reudcer.
 * His task is to aggregate all the local vectors to one vector and upload it to the origin cluster
 */
public class FinalVectorCreator {
    public static class ClusterReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            while (values.iterator().hasNext()){
                context.write(key,values.iterator().next());
            }
        }
    }
}