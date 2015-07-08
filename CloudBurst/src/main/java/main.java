import javax.swing.*;
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
    static String EuPath;
    static String EuropeDBindex;
    static String EgPath;
    static String EgyptDBindex;
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
    public static String[] params = new String[14];


    public static void RunVectorCreator() throws Exception {
        System.out.println("1");
        runEurope();
        runEgypt();


    }
    private static void runEurope() throws Exception {
        CreateList(1);
        configue(1);
        System.out.println("2");
        convertFiles(1);
        int i = CloudBurst.run(params);
        String[] alignmentParams = {params[2]};
        int j = PrintAlignments.main(alignmentParams);
        //VectorMapred.CreateVector(params[2] + "/results.txt", params[2] + "/vector", Integer.parseInt(params[8]), Integer.parseInt(params[9]));
        //VectorCreator.CreateFinalVector(params[2] + "/part-00000", DBlist,DBlength, SampleName,OutPath+"/",1);

    }



    private static void runEgypt() throws Exception {
        CreateList(2);
        configue(2);
        convertFiles(2);
        int i = CloudBurst.run(params);
        String[] alignmentParams = {params[2]};
        int j = PrintAlignments.main(alignmentParams);
        //VectorMapred.CreateVector(params[2] + "/results.txt", params[2] + "/vector", Integer.parseInt(params[8]), Integer.parseInt(params[9]));
        //VectorCreator.CreateFinalVector(params[2] + "/part-00000", DBlist,DBlength, SampleName,OutPath+"/",1);


    }

    private static void CreateList(int i) {
        DBlist = new ArrayList<String>();
        DBlength = new ArrayList<String>();
        BufferedReader br = null;
        try {
            if(i==1) {
                br = new BufferedReader(new FileReader(EuropeDBindex));
            }
            if(i==2){
                br = new BufferedReader(new FileReader(EgyptDBindex));
            }
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
    //i = 1 Europe
    //i = 2 Egypt
    private static void configue(int i) {
        if(i==1) {
            params[0] = OutPath +"/"+ SampleName +"/Local/EU/CloudBurst/BinaryFiles/ref.br";
            params[1] = OutPath +"/"+ SampleName +"/Local/EU/CloudBurst/BinaryFiles/qry.br";
            params[2] = OutPath +"/"+ SampleName +"/Local/EU/Vector/";
        }
        else if(i == 2){
            params[0] = OutPath +"/"+ SampleName +"/Local/Egypt/CloudBurst/BinaryFiles/ref.br";
            params[1] = OutPath +"/"+ SampleName +"/Local/Egypt/CloudBurst/BinaryFiles/qry.br";
            params[2] = OutPath +"/"+ SampleName +"/Local/Egypt/Vector/";
        }
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
    }

    private static void convertFiles(int i) {
        String[] params2 = new String[3];
        params2[0] = qpath;
        if (i == 1) {
            params2[1] = OutPath + "/" + SampleName + "/Local/EU/CloudBurst/BinaryFiles/qry.br";
        }
        if (i == 2) {
            params2[1] = OutPath + "/" + SampleName + "/Local/Egypt/CloudBurst/BinaryFiles/qry.br";
        }
        params2[2] = "Q";
        try {
            ConvertFastaForCloud.main(params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (i == 1) {
            params2[0] = EuPath;
            params2[1] = OutPath + "/" + SampleName + "/Local/EU/CloudBurst/BinaryFiles/ref.br";
        }
        if (i == 2) {
            params2[0] = EgPath;
            params2[1] = OutPath + "/" + SampleName + "/Local/Egypt/CloudBurst/BinaryFiles/ref.br";
        }
        params2[2] = "R";
        try {
            ConvertFastaForCloud.main(params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        //SampleName SampleFilePath HealthySamplePath EuropeDbPath EuropeDbIndexFilePath EgyptDbPath EgyptDbIndexFilePath OutputPath
        // MaxReadLength MinReadLength K AllowDiff ShowResults NumOfMappers NumOfReducers BlockSize Redundancy
        if(args.length != 17){
            System.out.println(args.length + " " + args[0]);
            System.out.println("SampleName SampleFilePath HealthySamplePath EuropeDbPath EuropeDbIndexFilePath EgyptDbPath EgyptDbIndexFilePath OutputPath MaxReadLength MinReadLength K AllowDiff ShowResults NumOfMappers NumOfReducers BlockSize Redundancy");
            return;
        }

        SampleName =args[0];
        qpath = args[1];
        hpath = args[2];
        EuPath = args[3];
        EuropeDBindex = args[4];
        EgPath = args[5];
        EgyptDBindex = args[6];
        OutPath = args[7];

        maxlength = (args[8]);
        minlength = (args[9]);
        CBk = (args[10]);
        CBdiff = (args[11]);
        ShowResults = (args[12]);
        mapnum = (args[13]);
        reducenum = (args[14]);
        blocksize = (args[15]);
        redundancy = (args[16]);
        RunVectorCreator();


    }
}
