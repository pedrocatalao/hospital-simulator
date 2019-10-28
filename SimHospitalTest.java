import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimHospitalTest {

    SimHospital sm;

    @Before
    public void initTest() {
        sm = new SimHospital();
    }

    @Test
    public void testFeverWithParacetamol() throws Exception {
        String[] args = {"F,F", "P"};

        sm.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:2,X:0", sm.prettyResult());
    }

    @Test(expected = Exception.class)
    public void testInvalidState() throws Exception {
        String[] args = {"F,K", "P"};

        sm.runSimulation(args);
    }

}