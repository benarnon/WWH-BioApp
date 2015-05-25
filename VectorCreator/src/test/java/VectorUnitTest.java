import junit.framework.TestCase;

public class VectorUnitTest extends TestCase {



    public void testCreateFile2() throws Exception {
        String sampleName = "s1";
        VectorUnit test = new VectorUnit(sampleName);

        test.CreateFile2("/home/user/IdeaProjects/WWH-BIO/VectorCreator/src/test/resources/UnitVectorResources/Healthy/healthy","/home/user/IdeaProjects/WWH-BIO/VectorCreator/src/test/resources/UnitVectorResources/LocalVectors/","/home/user/IdeaProjects/WWH-BIO/VectorCreator/src/test/resources/UnitVectorResources/out/","Sample1");
        System.out.println(test.toString());
    }

    public void testAddLocal2Global() throws Exception {

    }
}