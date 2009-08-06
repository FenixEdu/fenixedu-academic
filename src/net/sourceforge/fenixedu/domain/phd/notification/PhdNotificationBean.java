package net.sourceforge.fenixedu.domain.phd.notification;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class PhdNotificationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PhdNotificationType type;

    private DomainReference<PhdProgramCandidacyProcess> candidacyProcess;

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
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<PhdProgramCandidacyProcess>(candidacyProcess)
		: null;
    }

}
