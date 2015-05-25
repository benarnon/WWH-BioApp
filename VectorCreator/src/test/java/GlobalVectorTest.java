import junit.framework.TestCase;

public class GlobalVectorTest extends TestCase {

    public void testSetTuple() throws Exception {
        GlobalResTuple tuple = new GlobalResTuple("pathogen1",(float)0.5,"Egypt");
        GlobalVector tmp = new GlobalVector("sample1");
        tmp.setTuple(tuple);
        assertEquals(tmp.getResTuple(0).getPathogen(),"pathogen1");
        assertEquals(tmp.getResTuple(0).getScore(),(float)0.5);
        assertEquals(tmp.getResTuple(0).getClusterID(),"Egypt");
        assertEquals(tmp.getResTuple("pathogen1").getPathogen(),"pathogen1");
        assertEquals(tmp.getResTuple("pathogen1").getScore(),(float)0.5);
        assertEquals(tmp.getResTuple("pathogen1").getClusterID(),"Egypt");
        assertEquals(tmp.getResTuple(1),null);
    }


    public void testPath2vector() throws Exception {
        GlobalVector test,ans;
        test =new GlobalVector();
        ans = test.path2vector("/home/user/IdeaProjects/WWH-BIO/VectorCreator/src/test/resources/GlobalVectorResources/Sample1-GlobalVector");
        assertEquals(ans.getSampleName(),"test");
        GlobalResTuple tmp = ans.getResTuple("p2");
        assertEquals(tmp.getClusterID(),"Egypt");
        assertEquals(tmp.getScore(),(float)0.1);
        assertEquals(tmp.getPathogen(),"p2");

    }
}