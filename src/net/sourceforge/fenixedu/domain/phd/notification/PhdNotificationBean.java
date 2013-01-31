package net.sourceforge.fenixedu.domain.phd.notification;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class PhdNotificationBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private PhdNotificationType type;

	private PhdProgramCandidacyProcess candidacyProcess;

	public PhdNotificationBean(PhdProgramCandidacyProcess candidacyProcess) {
		setCandidacyProcess(candidacyProcess);
	}

	public PhdNotificationType getType() {
		return type;
	}

	public void setType(PhdNotificationType type) {
		this.type = type;
	}

	public PhdProgramCandidacyProcess getCandidacyProcess() {
		return this.candidacyProcess;
	}

	public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
		this.candidacyProcess = candidacyProcess;
	}

}
