import org.apache.avro.mapreduce.AvroSequenceFileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;



import java.io.IOException;
//TODO migrate to yarn -DONE
public class CloudBurst {	
	
	// Make sure this number is longer than the longest read
	public static final int CHUNK_OVERLAP = 1024;
	//------------------------- alignall --------------------------
	// Setup and run the hadoop job for running the alignment



    public static void alignall(String refpath,
                                      String qrypath,
                                      String outpath,
                                      int MIN_READ_LEN,
                                      int MAX_READ_LEN,
                                      int K,
                                      int ALLOW_DIFFERENCES,
                                      boolean FILTER_ALIGNMENTS,
                                      int NUM_MAP_TASKS,
                                      int NUM_REDUCE_TASKS,
                                      int BLOCK_SIZE,
                                      int REDUNDANCY) throws IOException, Exception
	{


        int SEED_LEN   = MIN_READ_LEN / (K+1);
		int FLANK_LEN  = MAX_READ_LEN-SEED_LEN+K;
		
		System.out.println("refath: "            + refpath);
		System.out.println("qrypath: "           + qrypath);
		System.out.println("outpath: "           + outpath);
		System.out.println("MIN_READ_LEN: "      + MIN_READ_LEN);
		System.out.println("MAX_READ_LEN: "      + MAX_READ_LEN);
		System.out.println("K: "                 + K);
		System.out.println("SEED_LEN: "          + SEED_LEN);
		System.out.println("FLANK_LEN: "         + FLANK_LEN);
		System.out.println("ALLOW_DIFFERENCES: " + ALLOW_DIFFERENCES);
		System.out.println("FILTER_ALIGNMENTS: " + FILTER_ALIGNMENTS);
		System.out.println("NUM_MAP_TASKS: "     + NUM_MAP_TASKS);
		System.out.println("NUM_REDUCE_TASKS: "  + NUM_REDUCE_TASKS);
		System.out.println("BLOCK_SIZE: "        + BLOCK_SIZE);
		System.out.println("REDUNDANCY: "        + REDUNDANCY);
		
		Configuration conf = new Configuration(true);

        Job job = new Job(conf,"CloudBurst");
		//conf.setJobName("CloudBurst"); MV1
        job.setNumReduceTasks(NUM_REDUCE_TASKS); // MV2

		//conf.setNumMapTasks(NUM_MAP_TASKS); TODO find solution for mv2
		//conf.setNumReduceTasks(NUM_REDUCE_TASKS);MV1

		// old style
		//conf.addInputPath(new Path(refpath));
		//conf.addInputPath(new Path(qrypath));
		
		// new style

		FileInputFormat.addInputPath(job, new Path(refpath));
		FileInputFormat.addInputPath(job, new Path(qrypath));

		conf.set("refpath",           refpath);
		conf.set("qrypath",           qrypath);
		conf.set("MIN_READ_LEN",      Integer.toString(MIN_READ_LEN));
		conf.set("MAX_READ_LEN",      Integer.toString(MAX_READ_LEN));
		conf.set("K",                 Integer.toString(K));
		conf.set("SEED_LEN",          Integer.toString(SEED_LEN));
		conf.set("FLANK_LEN",         Integer.toString(FLANK_LEN));
		conf.set("ALLOW_DIFFERENCES", Integer.toString(ALLOW_DIFFERENCES));
		conf.set("BLOCK_SIZE",        Integer.toString(BLOCK_SIZE));
		conf.set("REDUNDANCY",        Integer.toString(REDUNDANCY));
		conf.set("FILTER_ALIGNMENTS", (FILTER_ALIGNMENTS ? "1" : "0"));

        job.setMapperClass(MerReduce.MapClass.class);
		//conf.setMapperClass(MerReduce.MapClass.class);
		
		job.setInputFormatClass(SequenceFileInputFormat.class);
        //conf.setInputFormat(SequenceFileInputFormat.class);

        job.setMapOutputKeyClass(BytesWritable.class);//mv2
        //conf.setMapOutputKeyClass(BytesWritable.class);mv1

        job.setMapOutputValueClass(BytesWritable.class);//mv2
		//conf.setMapOutputValueClass(BytesWritable.class);mv1

		// The order of seeds is not important, but make sure the reference seeds are seen before the qry seeds
		job.setPartitionerClass(MerReduce.PartitionMers.class); // mv2
        //conf.setPartitionerClass(MerReduce.PartitionMers.class);
		job.setGroupingComparatorClass(MerReduce.GroupMersWC.class); //mv2 TODO
        //conf.setOutputValueGroupingComparator(MerReduce.GroupMersWC.class);
		
		job.setReducerClass(MerReduce.ReduceClass.class);
        //conf.setReducerClass(MerReduce.ReduceClass.class);
		//conf.setOutputKeyClass(IntWritable.class);
        job.setOutputKeyClass(IntWritable.class);

        //conf.setOutputValueClass(BytesWritable.class);
        job.setOutputValueClass(BytesWritable.class);

        //conf.setOutputFormat(SequenceFileOutputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
		
		Path oPath = new Path(outpath);
		//conf.setOutputPath(oPath);
		FileOutputFormat.setOutputPath(job, oPath);
		System.err.println("  Removing old results");
		FileSystem.get(conf).delete(oPath);


        //JobClient rj = JobClient.runJob(conf);
        int code = job.waitForCompletion(true) ? 0 : 1;

		System.err.println("CloudBurst Finished");
	}
	
	
	//------------------------- filter --------------------------
	// Setup and run the hadoop job for filtering the alignments to just report unambiguous bests
	
	public static void filter(String alignpath, 
			                  String outpath,
                              int nummappers,
                              int numreducers) throws IOException, Exception
    {
		System.out.println("NUM_FMAP_TASKS: "     + nummappers);
		System.out.println("NUM_FREDUCE_TASKS: "  + numreducers);

        // Create configuration
        Configuration conf = new Configuration(true);//mv2

        // Create job
        Job job = new Job(conf, "WordCount");//mv2
        job.setJarByClass(FilterAlignments.class);//mv2


        //JobConf conf = new JobConf(FilterAlignments.class);//mv1
		//conf.setJobName("FilterAlignments");//mv1

        job.setNumReduceTasks(numreducers);//mv2
		//conf.setNumMapTasks(nummappers);//TODO find solution for mv2
		//conf.setNumReduceTasks(numreducers);//mv1
		
		// old style
		//conf.addInputPath(new Path(alignpath));
		FileInputFormat.addInputPath(job, new Path(alignpath));

        job.setMapperClass(FilterAlignments.FilterMapClass.class);//mv2
        //conf.setMapperClass(FilterAlignments.FilterMapClass.class);//mv1

        job.setInputFormatClass(SequenceFileInputFormat.class);//mv2
        //conf.setInputFormat(SequenceFileInputFormat.class);mv1

        job.setMapOutputKeyClass(IntWritable.class);//mv2
        //conf.setMapOutputKeyClass(IntWritable.class);mv1

        job.setMapOutputValueClass(BytesWritable.class);//mv2
        //conf.setMapOutputValueClass(BytesWritable.class);mv1

        job.setInputFormatClass(SequenceFileInputFormat.class);//mv2
        //conf.setInputFormat(SequenceFileInputFormat.class);mv1

        job.setMapOutputKeyClass(IntWritable.class);
        //conf.setMapOutputKeyClass(IntWritable.class);

        job.setMapOutputValueClass(BytesWritable.class);
        //conf.setMapOutputValueClass(BytesWritable.class);

        job.setCombinerClass(FilterAlignments.FilterCombinerClass.class);
        //conf.setCombinerClass(FilterAlignments.FilterCombinerClass.class);

        job.setReducerClass(FilterAlignments.FilterReduceClass.class);
        //conf.setReducerClass(FilterAlignments.FilterReduceClass.class);

        job.setOutputKeyClass(IntWritable.class);
        //conf.setOutputKeyClass(IntWritable.class);

        job.setOutputValueClass(BytesWritable.class);
        //conf.setOutputValueClass(BytesWritable.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        //conf.setOutputFormat(SequenceFileOutputFormat.class);

		Path oPath = new Path(outpath);
		FileOutputFormat.setOutputPath(job, oPath);
		//conf.setOutputPath(oPath);
		System.err.println("  Removing old results");
		FileSystem.get(conf).delete(oPath);

        //JobClient rj = JobClient.runJob(conf);
        int code = job.waitForCompletion(true) ? 0 : 1;

		System.err.println("FilterAlignments Finished");
    }


	//------------------------- main --------------------------
	// Parse the command line options, run alignment and filtering
	public static void run(String[] args) throws  Exception{
        String refpath = null;
        String qrypath = null;
        String outpath = null;

        int K                = 0;
        int minreadlen       = 0;
        int maxreadlen       = 0;
        int allowdifferences = 0;

        int nummappers   = 1;
        int numreducers  = 1;
        int numfmappers  = 1;
        int numfreducers = 1;
        int blocksize    = 128;
        int redundancy   = 1;

        boolean filteralignments = false;

        int local = 0; // set to zero to use command line arguments

        if (local == 1)
        {
            refpath = "/home/user/IdeaProjects/CloudBurst/input/CloudBurst-small-sample/s_suis.br";
            qrypath = "/home/user/IdeaProjects/CloudBurst/input/CloudBurst-small-sample/100k.br";
            outpath = "/home/user/IdeaProjects/CloudBurst/input/CloudBurst-small-sample/results";
            minreadlen = 36;
            maxreadlen = 36;

            K = 2;
            allowdifferences = 0;
            filteralignments = false;
            redundancy       = 1;
        }
        else if (local == 2)
        {
            refpath = "/user/guest/cloudburst/short.ref.br";
            qrypath = "/user/guest/cloudburst/short.qry.br";
            outpath = "/user/guest/br-results";
            minreadlen = 32;
            maxreadlen = 32;

            K = 2;
            allowdifferences = 1;
            filteralignments = false;
            redundancy       = 1;
        }
        else if (args.length != 14)
        {
            System.err.println("Usage: CloudBurst refpath qrypath outpath minreadlen maxreadlen k allowdifferences filteralignments #mappers #reduces #fmappers #freducers blocksize redundancy");

            System.err.println();
            System.err.println("1.  refpath:          path in hdfs to the reference file");
            System.err.println("2.  qrypath:          path in hdfs to the query file");
            System.err.println("3.  outpath:          path to a directory to store the results (old results are automatically deleted)");
            System.err.println("4.  minreadlen:       minimum length of the reads");
            System.err.println("5.  maxreadlen:       maximum read length");
            System.err.println("6.  k:                number of mismatches / differences to allow (higher number requires more time)");
            System.err.println("7.  allowdifferences: 0: mismatches only, 1: indels as well");
            System.err.println("8.  filteralignments: 0: all alignments,  1: only report unambiguous best alignment (results identical to RMAP)");
            System.err.println("9.  #mappers:         number of mappers to use.              suggested: #processor-cores * 10");
            System.err.println("10. #reduces:         number of reducers to use.             suggested: #processor-cores * 2");
            System.err.println("11. #fmappers:        number of mappers for filtration alg.  suggested: #processor-cores");
            System.err.println("12. #freducers:       number of reducers for filtration alg. suggested: #processor-cores");
            System.err.println("13. blocksize:        number of qry and ref tuples to consider at a time in the reduce phase. suggested: 128");
            System.err.println("14. redundancy:       number of copies of low complexity seeds to use. suggested: # processor cores");

            return;
        }
        else
        {
            refpath          = args[0];
            qrypath          = args[1];
            outpath          = args[2];
            minreadlen       = Integer.parseInt(args[3]);
            maxreadlen       = Integer.parseInt(args[4]);
            K                = Integer.parseInt(args[5]);
            allowdifferences = Integer.parseInt(args[6]);
            filteralignments = Integer.parseInt(args[7]) == 1;
            nummappers       = Integer.parseInt(args[8]);
            numreducers      = Integer.parseInt(args[9]);
            numfmappers      = Integer.parseInt(args[10]);
            numfreducers     = Integer.parseInt(args[11]);
            blocksize        = Integer.parseInt(args[12]);
            redundancy       = Integer.parseInt(args[13]);
        }

        if (redundancy < 1) { System.err.println("minimum redundancy is 1"); return; }

        if (maxreadlen > CHUNK_OVERLAP)
        {
            System.err.println("Increase CHUNK_OVERLAP for " + maxreadlen + " length reads, and reconvert fasta file");
            return;
        }

        // start the timer
        Timer all = new Timer();

        String alignpath = outpath;
        if (filteralignments) { alignpath += "-alignments"; }


        // run the alignments
        Timer talign = new Timer();
        alignall(refpath, qrypath, alignpath, minreadlen, maxreadlen, K, allowdifferences, filteralignments,
                nummappers, numreducers, blocksize, redundancy);
        System.err.println("Alignment time: " + talign.get());


        // filter to report best alignments
        if (filteralignments)
        {
            Timer tfilter = new Timer();
            filter(alignpath, outpath, numfmappers, numfreducers);

            System.err.println("Filtering time: " + tfilter.get());
        }

        System.err.println("Total Running time:  " + all.get());

    }

}
