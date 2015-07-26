package Enumeration;

import BasicComponent.DatabaseIndex;
import FeatureRelatedComponent.GeneUnit;
import BasicComponent.Sample;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

public class EnumarationProcess_QuasiTest extends TestCase {

    public void testWork() throws Exception {

    }

    public void testNested_appendBrothers() throws Exception {
/*
        String[] symptoms = {"fever" , "headache"};
        Sample sample = new Sample("testSample1" , new Date(System.currentTimeMillis()) , "Rambam" );
        EnumarationProcess_Quasi ep = new EnumarationProcess_Quasi();

//        System.out.println("cluster children amount start time = " + ep._treeRoot._children.size());

        //mock mutual genes
        ArrayList<GeneUnit> mutualGenesTest = new ArrayList<GeneUnit>();
        DatabaseIndex[] dbList = new DatabaseIndex[2];
        DatabaseIndex[] dbList2 = new DatabaseIndex[2];
        DatabaseIndex[] dbList3 = new DatabaseIndex[2];
        DatabaseIndex[] dbList4 = new DatabaseIndex[2];
        dbList[0] = new DatabaseIndex("gi","1");
        dbList[1] = new DatabaseIndex("ref","1");
        dbList2[0] = new DatabaseIndex("gi","2");
        dbList2[1] = new DatabaseIndex("ref","2");
        dbList3[0] = new DatabaseIndex("gi","3");
        dbList3[1] = new DatabaseIndex("ref","3");
        dbList4[0] = new DatabaseIndex("gi","4");
        dbList4[1] = new DatabaseIndex("ref","4");
        GeneUnit gene1 = new GeneUnit("AR1" , 1000 , dbList);
        GeneUnit gene2 = new GeneUnit("PI1" , 1000 , dbList2);
        GeneUnit gene3 = new GeneUnit("AR2" , 1000 , dbList3);
        GeneUnit gene4 = new GeneUnit("PI2" , 1000 , dbList4);

        mutualGenesTest.add(gene1);
        mutualGenesTest.add(gene2);
        mutualGenesTest.add(gene3);
        mutualGenesTest.add(gene4);


        sample.addGenome(gene1);
        sample.addGenome(gene2);
        sample.addGenome(gene3);

        ep.work(new ArrayList<Sample>(), mutualGenesTest);
        System.out.println("cluster children amount start time = " + ep._treeRoot._children.size());
        System.out.println(ep.nested_appendBrothers(sample));
        System.out.println("cluster children amount finish time = " + ep._treeRoot._children.size());
*/
    }

    public void testNested_deletePath() throws Exception {

    }

    public void testNested_genrateCliques() throws Exception {

    }
}