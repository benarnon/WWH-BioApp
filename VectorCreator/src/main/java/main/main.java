package main;

import CreateVector.VectorCreator;
import CloudBurst.cloudBurst;
import CreateVector.VectorMapred;
import com.sun.java.util.jar.pack.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/6/15.
 */
public class main {
    static String SampleName;
    static String qpath;
    static String hpath;
    static String PathogenDbPath;
    static String PathogenDbIndexPath;
    static String OutPath;

    static String maxlength;
    static String  minlength;
    static String CBk;//K for CloudBurst
    static String CBdiff;
    static String ShowResults;
    static String mapnum;
    static String reducenum;
    static String blocksize;
    static String redundancy;
    //General
    static List<String> DBlist = new ArrayList<String>();
    static List<String> DBlength = new ArrayList<String>();
    public static String[] params = new String[15];


    public static void RunVectorCreator() throws Exception {
        CreateList();
        convertFiles();
        configue();
        String PathogenLength = toStringGeneLength();
        int i = cloudBurst.run(params);
        VectorMapred.CreateVector(OutPath + "/" + SampleName +"/Local/Vector/" , OutPath + "/" + SampleName +"/Local/finalVecor/", 1 ,1,PathogenLength);

    }

    private static void CreateList() {
        DBlist = new ArrayList<String>();
        DBlength = new ArrayList<String>();
        BufferedReader br = null;
        try {
                br = new BufferedReader(new FileReader(PathogenDbIndexPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line = null;
            if (br != null) {
                line = br.readLine();
            }

            while (line != null) {
                String id = line.split("\t")[0].substring(1);
                String length = line.split("\t")[1];
                DBlist.add(id);
                DBlength.add(length);
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

    }

    private static void configue() {
        params[0] = OutPath +"/"+ SampleName +"/Local/CloudBurst/BinaryFiles/ref.br";
        params[1] = OutPath +"/"+ SampleName +"/Local/CloudBurst/BinaryFiles/qry.br";
        params[2] = OutPath +"/"+ SampleName +"/Local/Bed-File/";

        params[3] = minlength;
        params[4] = maxlength;
        params[5] = CBk;
        params[6] = CBdiff;
        int filter = 0;
        params[7] = Integer.toString(filter);
        params[8] = mapnum;
        params[9] = reducenum;
        params[10] = "1";
        params[11] = "1";
        params[12] = blocksize;
        params[13] = redundancy;
        params[14] = toStringFastaList();
    }

    private static String toStringFastaList() {
        String ans = CloudBurst.ConvertFastaForCloud.FastaList[0];
        for (int i = 1; i < CloudBurst.ConvertFastaForCloud.FastaList.length ; i++) {
            ans = ans + "$" + CloudBurst.ConvertFastaForCloud.FastaList[i];
        }
        return ans;
    }

    private static String toStringGeneLength() throws IOException {
        String ans= "";
        BufferedReader br = new BufferedReader(new FileReader(PathogenDbIndexPath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            ans = sb.toString();
        } finally {
            br.close();
            System.out.println(ans);
            return ans;
        }
    }

    private static void convertFiles() {
        String[] params2 = new String[3];
        params2[0] = qpath;
        params2[1] = OutPath + "/" + SampleName + "/Local/CloudBurst/BinaryFiles/qry.br";
        params2[2] = "Q";
        try {
            CloudBurst.ConvertFastaForCloud.main(params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params2[0] = PathogenDbPath;
        params2[1] = OutPath + "/" + SampleName + "/Local/CloudBurst/BinaryFiles/ref.br";
        params2[2] = "R";
        try {
            CloudBurst.ConvertFastaForCloud.main(params2);
            System.out.println("ConvertFastaForCloud.FastaList: " + CloudBurst.ConvertFastaForCloud.FastaList.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        if(args.length != 15){
            System.out.println(args.length + " " + args[0]);
            System.out.println("SampleName SampleFilePath HealthySamplePath PathogenDbPath PathogenDbIndexFilePath OutputPath MaxReadLength MinReadLength K AllowDiff ShowResults NumOfMappers NumOfReducers BlockSize Redundancy");
            return;
        }

        SampleName =args[0];
        qpath = args[1];
        hpath = args[2];
        PathogenDbPath = args[3];
        PathogenDbIndexPath = args[4];
        OutPath = args[5];

        maxlength = (args[6]);
        minlength = (args[7]);
        CBk = (args[8]);
        CBdiff = (args[9]);
        ShowResults = (args[10]);
        mapnum = (args[11]);
        reducenum = (args[12]);
        blocksize = (args[13]);
        redundancy = (args[14]);
        RunVectorCreator();


    }
}
