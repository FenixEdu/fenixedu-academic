package net.sourceforge.fenixedu.domain.phd;

public enum PhdIndividualProgramProcessState {

    CANDIDACY(true),

    NOT_ADMITTED,

    CANCELLED,

    WORK_DEVELOPMENT(true),

    SUSPENDED,

    THESIS_DISCUSSION(true),

    FLUNKED,

    CONCLUDED;

    private boolean activeState;

    private PhdIndividualProgramProcessState(boolean activeState) {
	this.activeState = activeState;
    }

    private PhdIndividualProgramProcessState() {
	this(false);
    }

    public boolean isActive() {
	return activeState;
    }

}
