
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class GUI {
    JTextField qpath;
    JTextField maxlength;
    JTextField minlength;
    JComboBox cbK;
    JCheckBox CBdiff;
    JCheckBox ShowResults;
    JTextField mapnum;
    JTextField reducenum;
    JTextField blocksize;
    JTextField redundancy;
    JTextArea Terminal;
    JButton submit;
    JTextField SamplesPath;
    JTextField MatrixPath;
    JButton coClusterButton;
    JPanel gui;
    JTextField fastaPath;
    JTextField brPath;
    JCheckBox refQry;
    JButton convert;
    JRadioButton EuropeDB;
    JRadioButton EgyptDB;
    JTextField SampleName;
    private JTextField hpath;

    List<String> DBlist = new ArrayList<String>();
    List<String> DBlength = new ArrayList<String>();
    String[] params = new String[14];


    public void runEurope() throws Exception {
        CreateList2(1);
        configue2(1);
        convertFiles2(1);
        CloudBurst.run(params);
        String[] alignmentParams = {params[2]};
        PrintAlignments.main(alignmentParams);
        VectorMapred.CreateVector(params[2] + "/results.txt", params[2] + "/vector", Integer.parseInt(params[8]), Integer.parseInt(params[9]));
        VectorCreator.CreateFinalVector(params[2] + "/vector/part-00000", DBlist,DBlength, SampleName.getText(),"/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/",1);
    }

    public void runEgypt() throws Exception {
        CreateList2(2);
        configue2(2);
        convertFiles2(2);
        CloudBurst.run(params);
        String[] alignmentParams = {params[2]};
        PrintAlignments.main(alignmentParams);
        VectorMapred.CreateVector(params[2] + "/results.txt", params[2] + "/vector", Integer.parseInt(params[8]), Integer.parseInt(params[9]));
        VectorCreator.CreateFinalVector(params[2] + "/vector/part-00000", DBlist,DBlength, SampleName.getText(),"/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/",2);
    }



    public GUI() {
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(EuropeDB.isSelected()){
                    try {
                       runEurope();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(EgyptDB.isSelected()){
                    try {
                       runEgypt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (EuropeDB.isSelected() && EgyptDB.isSelected()) {
                    //Start with europe
                    try {
                        VectorUnit unitor = new VectorUnit(SampleName.getText());
                        unitor.CreateFile2(hpath.getText(),"/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/", "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/", SampleName.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                try {
                    Runtime.getRuntime().exec("libreoffice -calc /home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/"+ SampleName.getText() + "-GlobalVector") ;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        coClusterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MatrixCreator creator;
                creator = new MatrixCreator();
                creator.CreateMatrix2(SamplesPath.getText());
                creator.CreateMatrixFile2("/home/user/IdeaProjects/WWH-BIO/out/MatrixCreator/");
                try {
                    Runtime.getRuntime().exec("libreoffice -calc /home/user/IdeaProjects/WWH-BIO/out/MatrixCreator/matrix.txt") ;
                    Runtime.getRuntime().exec("Rscript /home/user/Rprojects/Co-cluster/Co-cluster.R");
                    Runtime.getRuntime().exec("xdg-open /home/user/IdeaProjects/WWH-BIO/out/R-script/Heatmap");
                    BiclusterResults biclusterResults = new BiclusterResults("/home/user/IdeaProjects/WWH-BIO/out/R-script/Bicluster",creator);
                    biclusterResults.createBiclusterFile("/home/user/IdeaProjects/WWH-BIO/out/Bicluster/");
                    Runtime.getRuntime().exec("libreoffice -calc /home/user/IdeaProjects/WWH-BIO/out/Bicluster/Bicluster.txt") ;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] params = new String[3];
                params[0] = fastaPath.getText();
                params[1] = brPath.getText();
                if(refQry.isSelected())
                    params[2] = "R";
                else
                    params[2] = "Q";
                try {
                    ConvertFastaForCloud.main(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Terminal.append("Convert file succed\n");


            }

        });


    }

    //i=1 Europe
    //i=2 Egypt
    private void CreateList2(int i) {
        DBlist = new ArrayList<String>();
        DBlength = new ArrayList<String>();
        BufferedReader br = null;
        try {
            if(i==1) {
                br = new BufferedReader(new FileReader("/home/user/IdeaProjects/WWH-BIO/Gui/src/main/resources/EUindex.txt"));
            }
            if(i==2){
                br = new BufferedReader(new FileReader("/home/user/IdeaProjects/WWH-BIO/Gui/src/main/resources/EgyptIndex.txt"));
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
    private void configue2(int i) {
        params[0] = "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/BinaryFiles/ref.br";
        params[1] = "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/BinaryFiles/qry.br";
        if(i==1) {
            params[2] = "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/EU";
        }
        else if(i == 2){
            params[2] = "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/Egypt";
        }
        params[3] = minlength.getText();
        params[4] = maxlength.getText();
        params[5] = cbK.getSelectedItem().toString();
        int diff = ((CBdiff.isSelected()) ? 1 : 0);
        params[6] = Integer.toString(diff);
        int filter = 0;
        params[7] = Integer.toString(filter);
        params[8] = mapnum.getText();
        params[9] = reducenum.getText();
        params[10] = "1";
        params[11] = "1";
        params[12] = blocksize.getText();
        params[13] = redundancy.getText();
    }
    //i = 1 Europe
    //i = 2 Egypt
    private void convertFiles2(int i) {
        String[] params2 = new String[3];
        params2[0] = qpath.getText();
        params2[1] = "/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/BinaryFiles/qry.br";
        params2[2] = "Q";
        try {
            ConvertFastaForCloud.main(params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Terminal.append("Convert query file succed\n");
        if(i==1) {
            params2[0] = "/home/user/IdeaProjects/WWH-BIO/Gui/src/main/resources/EUdb.fa";
        }
        if(i==2){
            params2[0] = "/home/user/IdeaProjects/WWH-BIO/Gui/src/main/resources/EgyptDb.fa";
        }
        params2[1] ="/home/user/IdeaProjects/WWH-BIO/out/VectorCreator/Vectors/" + SampleName.getText() +"/BinaryFiles/ref.br";
        params2[2] = "R";
        try {
            ConvertFastaForCloud.main(params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Terminal.append("Convert reference file succed\n");
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().gui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
