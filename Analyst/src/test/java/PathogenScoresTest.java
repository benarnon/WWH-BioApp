import junit.framework.TestCase;

public class PathogenScoresTest extends TestCase {

    public void testGetName() throws Exception {
        PathogenScores tmp = new PathogenScores("name",new float[10]);
        assertEquals(tmp.getName(),"name");
    }

    public void testSetName() throws Exception {
        PathogenScores tmp = new PathogenScores(100);
        tmp.setName("name");
        assertEquals("name",tmp.getName());
    }


    public void testAddScore() throws Exception {

    }



    public void testGetScore() throws Exception {

    }
}