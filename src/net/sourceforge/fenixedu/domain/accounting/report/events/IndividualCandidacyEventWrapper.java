package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;

public class IndividualCandidacyEventWrapper implements Wrapper {
    IndividualCandidacyEvent event;

    public IndividualCandidacyEventWrapper(IndividualCandidacyEvent event) {
	this.event = event;
    }

    @Override
    public String getStudentNumber() {
	if(event.getPerson().hasStudent()) {
	    return event.getPerson().getStudent().getNumber().toString();
	}
	
	return "-";
    }

    @Override
    public String getStudentName() {
	return event.getPerson().getName();
    }

    @Override
    public String getRegistrationStartDate() {
	return "-";
    }

    @Override
    public String getExecutionYear() {
	return event.getIndividualCandidacy().getCandidacyProcess().getCandidacyProcess().getCandidacyExecutionInterval()
		.getName();
    }

    @Override
    public String getDegreeName() {
	return "-";
    }

    @Override
    public String getDegreeType() {
	return "-";
    }

    @Override
    public String getPhdProgramName() {
	return "-";
    }

    @Override
    public String getEnrolledECTS() {
	return "-";
    }

    @Override
    public String getRegime() {
	return "-";
    }

    @Override
    public String getEnrolmentModel() {
	return "-";
    }

    @Override
    public String getResidenceYear() {
	return "-";
    }

    @Override
    public String getResidenceMonth() {
	return "-";
    }

    @Override
    public String getStudiesType() {
	return "-";
    }

}
