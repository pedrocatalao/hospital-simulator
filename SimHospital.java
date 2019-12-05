import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SimHospital {

    private Map<String, Integer> outcome = Stream.of(States.values())
            .collect(Collectors.toMap(e -> e.code, e -> 0));

    enum States {
        Fever("F"), Healthy("H"), Diabetes("D"), Tuberculosis("T"), Dead("X");

        String code;

        States(String code) {
            this.code = code;
        }

        public static States getState(String code) throws Exception {
            for (States state : States.values()) {
                if (state.code.toLowerCase().equals(code.toLowerCase()))
                    return state;
            }

            throw new Exception ("Invalid subject state: " + code);
        }
    }

    enum Drugs {
        Aspirin("As"), Antibiotic("An"), Insulin("I"), Paracetamol("P");

        String code;

        Drugs(String code) {
            this.code = code;
        }

        public static Drugs getDrug(String code) throws Exception {
            for (Drugs drug : Drugs.values()) {
                if (drug.code.toLowerCase().equals(code.toLowerCase()))
                    return drug;
            }

            throw new Exception ("Invalid drug code: " + code);
        }
    }

    class Subject {
        States state;

        Subject(States state) {
            this.state = state;
        }

        void takeDrugs(List<Drugs> drugs) {

            if (drugs.containsAll(Arrays.asList(Drugs.Aspirin, Drugs.Paracetamol)))
                state = States.Dead;

            switch (state) {
                case Fever:
                    if (drugs.contains(Drugs.Aspirin) || drugs.contains(Drugs.Paracetamol))
                        state = States.Healthy;
                    break;

                case Healthy:
                    if (drugs.contains(Drugs.Antibiotic) && drugs.contains(Drugs.Insulin))
                        state = States.Fever;
                    if (drugs.contains(Drugs.Aspirin) || drugs.contains(Drugs.Paracetamol))
                        state = States.Healthy;
                    break;

                case Tuberculosis:
                    if (drugs.contains(Drugs.Antibiotic))
                        state = States.Healthy;
                    break;

                case Diabetes:
                    if (!drugs.contains(Drugs.Insulin))
                        state = States.Dead;
                    // purposely no break here, feeling lucky ;)

                case Dead:
                    Random r = new Random();
                    if (r.nextInt(1000000) == 1337) // Flying Spaghetti Monster miracle
                        state = States.Healthy;
            }

            outcome.merge(state.code, 1, Integer::sum);
        }
    }

    void runSimulation(String[] args) throws Exception {
        List<Drugs> drugs = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();

        if(args.length > 1)
            for (String drug : args[1].split(","))
                drugs.add(Drugs.getDrug(drug));

        if(args.length > 0)
            for (String state : args[0].split(","))
                subjects.add(new Subject(States.getState(state)));

        subjects.forEach(subject -> subject.takeDrugs(drugs));
    }

    String formatOutcome() {
        return outcome.keySet().stream()
                .map(key -> key + ":" + outcome.get(key))
                .collect(Collectors.joining(","));
    }

    public static void main(String[] args) throws Exception {
        SimHospital sim = new SimHospital();
        sim.runSimulation(args);
        System.out.println(sim.formatOutcome());
    }
}