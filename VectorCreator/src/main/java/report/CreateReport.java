package report;


import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.data.xy.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
public class CreateReport {
    int iExitValue;
    String sCommandString;
    /*
    * args[0] - demo.R path
    * args[1] - sources path
    * args[2] - servlet path
    * args[3] - dbproperties path
    * */
    public static final String CSV_FILES_PATH = "/WWH-results/app/resources/outputs";
    public static final String DB_PROPERTIES_PATH = "/WWH-results/app/resources/dbproperties.json";
    public static final String SERVER_PATH = "/WWH-results/app";
    public static final String DB_PREFIX = "C";

    private void drawOutput(String csvsPath) throws FileNotFoundException {
        File[] sourceFolderList = new File(csvsPath).listFiles();
        for (File dbDir : sourceFolderList) {
            if (dbDir.isDirectory() ){
                String Dbname = dbDir.getName();
                System.out.println("Working on Cluster " + Dbname);
                String outDbPath = SERVER_PATH + "/" + Dbname;
                new File(outDbPath).mkdirs();
                File[] csvFiles = dbDir.listFiles();
                for (File csvFile : csvFiles) {
                    String filename = csvFile.getName().replace(".csv" , "");
                    System.out.println("Working on gene-unit " + filename);
                    String outGenePath = outDbPath + "/" +filename;
                    File outGeneDir = new File(outGenePath);
                    outGeneDir.mkdir();

                    BufferedReader br = null;
                    String line = "";
                    String cvsSplitBy = ",";
                    try {
                        br = new BufferedReader(new FileReader(csvFile));
                        br.readLine();
                        //parameters[0]-Depth , parameters[1]-coverage parameters[2]-integral parameters[3]-numofreads
                        String[] parameters = br.readLine().split(cvsSplitBy);
                        DefaultTableXYDataset dataSet = prepareDataFromBr(br);

                        String newFileOutPath = outGenePath+"/"+filename;
                        //create and print the chart to jpeg
                        JFreeChart chart = ChartFactory.createStackedXYAreaChart("Coverage Plot", "Position", "Base Count", dataSet);
                        try {
                            ChartUtilities.saveChartAsJPEG(new File(newFileOutPath + "_hist.jpeg"), chart, 500, 300);
                        } catch (IOException e) {
                            System.err.println("Problem occurred creating chart.");
                        }
                        System.out.println("Hist image created");
                        //Draw the depth circle
                        // Open a JPEG file, open a new buffered image
                        File imgF = new File(newFileOutPath+"_circ.jpeg");
                        BufferedImage img = new BufferedImage(1000,1000 , BufferedImage.TYPE_INT_RGB);
                        System.out.println("Circ image created");

                        // Obtain the Graphics2D context associated with the BufferedImage.
                        Graphics2D g = img.createGraphics();
                        int depth = (int) Math.round(Double.parseDouble(parameters[0])*100);
                        g.setColor(new Color(100,100,100));
                        g.draw(new Ellipse2D.Double(0,0,100,100));
                        //g.fillOval(1000, 1000, depth, depth);


                        ImageIO.write(img, "jpeg", imgF);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("Finisn to work on Cluster " + Dbname);
            }

        }
    }

    private DefaultTableXYDataset prepareDataFromBr(BufferedReader br) {

        XYSeries plusSet = new XYSeries("+",false , false);
        XYSeries minusSet = new XYSeries("-",false , false);
        DefaultTableXYDataset dataSet = new DefaultTableXYDataset();
        String line = "";
        String cvsSplitBy = ",";
        try {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                Double position = Double.parseDouble(values[0]);
                Double basecount = Double.valueOf(values[1]);
                if (values[2].equals("+"))
                    plusSet.add(position , basecount);
                else if (values[2].equals("-"))
                    minusSet.add(position,basecount);
            }
            dataSet.addSeries(plusSet);
            dataSet.addSeries(minusSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    private List<DB> createDBobj(String csvsPath) throws IOException {
        //initialize available databases
        Gson gson = new Gson();
        List<DB> dbs = new ArrayList<DB>();
        Type type = new TypeToken<List<DB>>(){}.getType();
        dbs = gson.fromJson(readFile(DB_PROPERTIES_PATH) , type);

        //add geneUnits to json
        File[] sourceFolderList = new File(csvsPath).listFiles();
        for (File dbDirectory : sourceFolderList) {
            if (dbDirectory.isDirectory()) {
                String dbFolderName = dbDirectory.getName();
                if (dbFolderName.startsWith(DB_PREFIX)) {
                    String dbId = dbFolderName;
                    DB tempDb = getDbWithId(dbId, dbs);
                    List<GeneUnit> tempGeneUnits = getGeneUnitsFromFolder(dbDirectory);
                    if (tempDb!=null)
                        tempDb.setGeneUnits(tempGeneUnits);
                }
            }
        }
        return dbs;
    }

    private List<GeneUnit> getGeneUnitsFromFolder(File dbDirectory) {
        List<GeneUnit> geneUnits = new ArrayList<GeneUnit>();
        File[] geneDirs = dbDirectory.listFiles();
        for (File geneDir : geneDirs) {
            String filename = geneDir.getName().replace(".csv","");
            GeneUnit tempGeneUnit = new GeneUnit(filename);
            tempGeneUnit.setDescription(filename+"_desc");
            geneUnits.add(tempGeneUnit);
        }
        return geneUnits;
    }

    private DB getDbWithId(String dbId , List<DB> dbs) {
        for (DB db : dbs) {
            if (db.getId().equals(dbId))
                return db;
        }
        return null;
    }

    private String readFile(String path) throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        CreateReport createReport = new CreateReport();
        System.out.println("After CreateReport");
        createReport.drawOutput(CSV_FILES_PATH);
        System.out.println("After draw output");
        List<DB> dbs = createReport.createDBobj(CSV_FILES_PATH);
        WWHClusters clusters = new WWHClusters(dbs);
        Gson gson = new Gson();
        String json = gson.toJson(clusters);
        PrintWriter p = new PrintWriter(new File(SERVER_PATH+"/testJson.json"));
        p.print(json);
        p.close();
    }




}
