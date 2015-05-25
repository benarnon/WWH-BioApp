import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.fs.FileSystem;


import java.io.IOException;


/**
 * Created by user on 3/1/15.
 */


public class VectorMapred {
    public static void CreateVector(String CBresults,
                                    String outpath,
                                    int nummappers,
                                    int numreducers) throws IOException, Exception
    {
        System.out.println("NUM_FMAP_TASKS: "     + nummappers);
        System.out.println("NUM_FREDUCE_TASKS: "  + numreducers);

        JobConf conf = new JobConf(VectorCreator.class);
        conf.setJobName("VectorCreator");
        conf.setNumMapTasks(nummappers);
        conf.setNumReduceTasks(numreducers);

        // old style
        //conf.addInputPath(new Path(alignpath));
        org.apache.hadoop.mapred.FileInputFormat.addInputPath(conf, new Path(CBresults));

        conf.setMapperClass(VectorCreator.ClusterMapper.class);

        //conf.setInputFormat(SequenceFileInputFormat.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(IntWritable.class);


        conf.setReducerClass(VectorCreator.ClusterReducer.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        //conf.setOutputFormat(SequenceFileOutputFormat.class);


        Path oPath = new Path(outpath);
        org.apache.hadoop.mapred.FileOutputFormat.setOutputPath(conf, oPath);
        //conf.setOutputPath(oPath);
        System.err.println("  Removing old results");
        FileSystem.get(conf).delete(oPath);


        JobClient.runJob(conf);

        System.err.println("Create Vector Finished");
    }
}
