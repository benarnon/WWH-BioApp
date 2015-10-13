package matrixCreator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by user on 10/11/15.
 */
public class MatrixMapred {
    private static final Log LOG = LogFactory.getLog(MatrixCreator.class);
    public static void MatrixCreator() throws IOException, Exception

    {
        Configuration conf = new Configuration(true);

        Job job = new Job(conf, "matrixCreator");
        FileInputFormat.addInputPath(job, new Path("/MatInput"));
        job.setJarByClass(MatrixCreator.class);
        job.setMapperClass(MatrixCreator.ClusterMapper.class);

        //conf.setInputFormat(SequenceFileInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(MatrixCreator.ClusterReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //conf.setOutputFormat(SequenceFileOutputFormat.class);


        Path oPath = new Path("/matrixOut");
        FileOutputFormat.setOutputPath(job, oPath);
        //conf.setOutputPath(oPath);
        System.err.println("  Removing old results");
        FileSystem.get(conf).delete(oPath);


        //JobClient rj = JobClient.runJob(conf);
        int code = job.waitForCompletion(true) ? 0 : 1;

        System.err.println("Create Vector Finished");
    }
}
