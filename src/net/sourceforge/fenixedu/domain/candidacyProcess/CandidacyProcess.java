package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionInterval;

import org.joda.time.DateTime;

abstract public class CandidacyProcess extends CandidacyProcess_Base {

    protected CandidacyProcess() {
	super();
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyPeriod() ? getCandidacyPeriod().getExecutionInterval() : null;
    }

    public DateTime getCandidacyStart() {
	return hasCandidacyPeriod() ? getCandidacyPeriod().getStart() : null;
    }

    public DateTime getCandidacyEnd() {
	return hasCandidacyPeriod() ? getCandidacyPeriod().getEnd() : null;
    }
    
    public boolean hasStarted() {
	return !getCandidacyStart().isAfterNow();
    }

    public boolean hasOpenCandidacyPeriod() {
	return hasCandidacyPeriod() && getCandidacyPeriod().isOpen();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
	return hasCandidacyPeriod() && getCandidacyPeriod().isOpen(date);
    }

    public boolean hasState() {
	return getState() != null;
    }

    public boolean isInStandBy() {
	return getState() == CandidacyProcessState.STAND_BY;
    }

    public boolean isSentToJury() {
	return getState() == CandidacyProcessState.SENT_TO_JURY;
    }

    public boolean isSentToCoordinator() {
	return getState() == CandidacyProcessState.SENT_TO_COORDINATOR;
    }

    public boolean isSentToScientificCouncil() {
	return getState() == CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL;
    }

    public boolean isPublished() {
	return getState() == CandidacyProcessState.PUBLISHED;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources").getString("label." + getClass().getName());
    }
}
