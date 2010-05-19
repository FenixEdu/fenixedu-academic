package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class PhdThesisJuryElementBean extends PhdParticipantBean {

    static private final long serialVersionUID = -5365333247731361583L;

    private PhdThesisProcess thesisProcess;
    private boolean reporter;

    public PhdThesisJuryElementBean(final PhdThesisProcess thesisProcess) {
	super(thesisProcess.getIndividualProgramProcess());
	setThesisProcess(thesisProcess);
    }

    public PhdThesisProcess getThesisProcess() {
	return thesisProcess;
    }

    public void setThesisProcess(PhdThesisProcess thesisProcess) {
	this.thesisProcess = thesisProcess;
    }

    public boolean isReporter() {
	return reporter;
    }

    public void setReporter(boolean reporter) {
	this.reporter = reporter;
    }

    public List<PhdParticipant> getExistingParticipants() {
	final List<PhdParticipant> result = new ArrayList<PhdParticipant>();
	for (final PhdParticipant participant : getIndividualProgramProcess().getParticipantsSet()) {
	    if (!participant.hasAnyThesisJuryElements()) {
		result.add(participant);
	    }
	}
	return result;
    }

}
