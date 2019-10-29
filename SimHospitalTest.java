import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimHospitalTest {

    private SimHospital sim;

    @Before
    public void initTest() {
        sim = new SimHospital();
    }

    @Test
    public void heals_fever_with_paracetamol() throws Exception {
        String[] args = {"F,F,F,F,F", "P"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:5,X:0", sim.formatOutcome());
    }

    @Test
    public void heals_fever_with_aspirin() throws Exception {
        String[] args = {"F,F,F,F,F", "As"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:5,X:0", sim.formatOutcome());
    }

    @Test
    public void aspirin_mixed_with_paracetamol_kills_subjects() throws Exception {
        String[] args = {"F,F,F,F,F", "As,P"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:0,X:5", sim.formatOutcome());
    }

    @Test
    public void diabetic_subjects_die_without_insulin() throws Exception {
        String[] args = {"D,D,D,D,D"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:0,X:5", sim.formatOutcome());
    }

    @Test
    public void healthy_subjects_get_fever_with_antibiotic_and_insulin() throws Exception {
        String[] args = {"H,H,H,H,H", "I,An"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:5,H:0,X:0", sim.formatOutcome());
    }

    @Test
    public void antibiotic_heals_tuberculosis() throws Exception {
        String[] args = {"T,T,T,T,T", "An"};

        sim.runSimulation(args);

        Assert.assertEquals("D:0,T:0,F:0,H:5,X:0", sim.formatOutcome());
    }

    @Test
    public void antibiotic_paracetamol_insulin_cocktail_is_good() throws Exception {
        String[] args = {"F,T,D,H,T", "An,P,I"};

        sim.runSimulation(args);

        Assert.assertEquals("D:1,T:0,F:0,H:4,X:0", sim.formatOutcome());
    }

    @Test(expected = Exception.class)
    public void invalid_patient_state_throws_exception() throws Exception {
        String[] args = {"F,K", "P"};

        sim.runSimulation(args);
    }

    @Test(expected = Exception.class)
    public void invalid_drug_code_throws_exception() throws Exception {
        String[] args = {"F,F", "J"};

        sim.runSimulation(args);
    }
}