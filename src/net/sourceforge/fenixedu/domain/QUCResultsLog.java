package net.sourceforge.fenixedu.domain;

public class QUCResultsLog extends QUCResultsLog_Base {

    public QUCResultsLog() {
        super();
    }

    public QUCResultsLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    public static QUCResultsLog createQUCResultsLog(Degree degree, ExecutionYear executionYear, String description) {
        return new QUCResultsLog(degree, executionYear, description);
    }

    public static QUCResultsLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createQUCResultsLog(degree, executionYear, label);
    }

    @Override
    public DegreeLogTypes getDegreeLogType() {
        return DegreeLogTypes.QUC_RESULTS;
    }

}