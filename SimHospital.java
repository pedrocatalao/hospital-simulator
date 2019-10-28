import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimHospital {

    private static Map<String, Integer> result = new HashMap<>();

    private static void initResult() {
        for(States state : States.values()) {
            result.put(state.code, 0);
        }
    }

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

    static class Subject {
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

                case Dead:
                    Random r = new Random();
                    if (r.nextInt(1000000) == 1337) // Flying Spaghetti Monster miracle
                        state = States.Healthy;
            }
        }
    }

    static void incrementStateCount(States state) {
        int count = result.get(state.code);
        result.put(state.code, count + 1);
    }

    public static void main(String[] args) throws Exception {
        List<Drugs> drugs = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();

        initResult();

        if(args.length > 1)
            for (String drug : args[1].split(","))
                drugs.add(Drugs.getDrug(drug));

        if(args.length > 0)
            for (String state : args[0].split(","))
                subjects.add(new Subject(States.getState(state)));

        for(Subject subject : subjects) {
            subject.takeDrugs(drugs);
            incrementStateCount(subject.state);
        }

        System.out.println(
                result.keySet().stream()
                .map(key -> key + ":" + result.get(key))
                .collect(Collectors.joining(",")));
    }
}