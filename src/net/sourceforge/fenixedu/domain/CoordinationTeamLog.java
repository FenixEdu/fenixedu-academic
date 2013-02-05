package net.sourceforge.fenixedu.domain;

public class CoordinationTeamLog extends CoordinationTeamLog_Base {

    public CoordinationTeamLog() {
        super();
    }

    public CoordinationTeamLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    public static CoordinationTeamLog createCoordinationTeamLog(Degree degree, ExecutionYear executionYear, String description) {
        return new CoordinationTeamLog(degree, executionYear, description);
    }

    public static CoordinationTeamLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key,
            String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createCoordinationTeamLog(degree, executionYear, label);
    }

    @Override
    public DegreeLogTypes getDegreeLogType() {
        return DegreeLogTypes.COORDINATION_TEAM;
    }

}
