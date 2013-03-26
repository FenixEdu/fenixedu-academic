package net.sourceforge.fenixedu.domain;

public class CandidaciesLog extends CandidaciesLog_Base {

    public CandidaciesLog() {
        super();
    }

    public CandidaciesLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    public static CandidaciesLog createCandidaciesLog(Degree degree, ExecutionYear executionYear, String description) {
        return new CandidaciesLog(degree, executionYear, description);
    }

    public static CandidaciesLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createCandidaciesLog(degree, executionYear, label);
    }

    @Override
    public DegreeLogTypes getDegreeLogType() {
        return DegreeLogTypes.CANDIDACIES;
    }

}
