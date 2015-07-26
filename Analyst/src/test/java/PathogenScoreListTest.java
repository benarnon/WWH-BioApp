import junit.framework.TestCase;


public class PathogenScoreListTest extends TestCase {

    public void testAddPathogen() throws Exception {
        /*
        PathogenScoreList pathogenScoreList = new PathogenScoreList(3);
        assertEquals(pathogenScoreList.getNumOfFiles(),3);
        pathogenScoreList.addPathogen("testPathogen");
        pathogenScoreList.addPathogen("testPathogen2");
        PathogenScores tmp = pathogenScoreList.getPathogenScore("testPathogen");
        PathogenScores tmp2 = pathogenScoreList.getPathogenScore("testPathogen2");
        assertEquals(tmp.getName(),"testPathogen");
        assertEquals(tmp2.getName(),"testPathogen2");
        */

    }

    public void testIsContain() throws Exception {
        /*
        PathogenScoreList pathogenScoreList = new PathogenScoreList(3);
        assertEquals(pathogenScoreList.getNumOfFiles(),3);
        pathogenScoreList.addPathogen("testPathogen");
        assertEquals(pathogenScoreList.isContain("testPathogen"),0);
        assertEquals(pathogenScoreList.isContain("Notexist"),-1);
        */

    }

    public void testAddScore() throws Exception {
        /*
        PathogenScoreList pathogenScoreList = new PathogenScoreList(3);
        pathogenScoreList.addPathogen("testPathogen");
        PathogenScores tmp = pathogenScoreList.getPathogenScore("testPathogen");
        float a = (float) 0.5;
        pathogenScoreList.addScore("testPathogen",a,1);
        assertEquals(pathogenScoreList.getPathogenScore("testPathogen").getScore(1),(float)0.5);
        System.out.println();
        assertEquals(pathogenScoreList.getPathogenScore("testPathogen").getScore(2),(float)0);
        */
    }


}