package net.sourceforge.fenixedu.domain;

public class ScientificCommissionLog extends ScientificCommissionLog_Base {

	public ScientificCommissionLog() {
		super();
	}

	public ScientificCommissionLog(Degree degree, ExecutionYear executionYear, String description) {
		super();
		if (getDegree() == null) {
			setDegree(degree);
		}
		if (getExecutionYear() == null) {
			setExecutionYear(executionYear);
		}
		setDescription(description);
	}

	public static ScientificCommissionLog createScientificCommissionLog(Degree degree, ExecutionYear executionYear,
			String description) {
		return new ScientificCommissionLog(degree, executionYear, description);
	}

	public static ScientificCommissionLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key,
			String... args) {
		final String label = generateLabelDescription(bundle, key, args);
		return createScientificCommissionLog(degree, executionYear, label);
	}

	@Override
	public DegreeLogTypes getDegreeLogType() {
		return DegreeLogTypes.SCIENTIFIC_COMISSION;
	}

}
