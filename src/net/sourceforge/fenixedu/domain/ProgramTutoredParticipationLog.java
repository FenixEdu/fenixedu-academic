package net.sourceforge.fenixedu.domain;

public class ProgramTutoredParticipationLog extends ProgramTutoredParticipationLog_Base {

	public ProgramTutoredParticipationLog() {
		super();
	}

	public ProgramTutoredParticipationLog(Degree degree, ExecutionYear executionYear, String description) {
		super();
		if (getDegree() == null) {
			setDegree(degree);
		}
		if (getExecutionYear() == null) {
			setExecutionYear(executionYear);
		}
		setDescription(description);
	}

	public static ProgramTutoredParticipationLog createProgramTutoredParticipationLog(Degree degree, ExecutionYear executionYear,
			String description) {
		return new ProgramTutoredParticipationLog(degree, executionYear, description);
	}

	public static ProgramTutoredParticipationLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key,
			String... args) {
		final String label = generateLabelDescription(bundle, key, args);
		return createProgramTutoredParticipationLog(degree, executionYear, label);
	}

	@Override
	public DegreeLogTypes getDegreeLogType() {
		return DegreeLogTypes.PROGRAM_TUTORED_PARTICIPATION;
	}

}
