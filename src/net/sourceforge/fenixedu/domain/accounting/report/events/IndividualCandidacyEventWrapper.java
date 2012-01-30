package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

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
	return getForExecutionYear().getName();
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

    @Override
    public String getTotalDiscount() {
	return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return !ExecutionYear.readByDateTime(event.getIndividualCandidacy().getCandidacyDate()).isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
	return ExecutionYear.readByDateTime(event.getIndividualCandidacy().getCandidacyDate());
    }

    @Override
    public AdministrativeOfficeType getRelatedAcademicOfficeType() {
	return AdministrativeOfficeType.DEGREE;
    }

}
