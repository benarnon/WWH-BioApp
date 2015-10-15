package treeCreator;//package treeCreator;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import treeCreator.TreeCreator;

/**
 * Created by gal on 9/2/2015.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{

        TreeCreator tree = new TreeCreator();
        for(int i = 0; i < 3; i++){
            tree.CreateTree("/home/user/IdeaProjects/WWH-BioApp_resources/ParseBicluster/bicluster" + i + ".json","/home/user/IdeaProjects/WWH-BioApp_resources/TreeCreator/midFiles/"
                    , "/home/user/IdeaProjects/WWH-BioApp_resources/WWH-results/app/views/maps/","/home/user/IdeaProjects/WWH-BioApp/Analyst/src/main/java/SeqTrakTreeCreator.R", new Integer(i).toString());
        }







    }
}
