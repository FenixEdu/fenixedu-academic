package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdEventWrapper implements Wrapper {
    PhdEvent event;

    public PhdEventWrapper(final PhdEvent event) {
	this.event = event;
    }

    @Override
    public String getStudentNumber() {
	if (event.getPerson().hasStudent()) {
	    return event.getPerson().getStudent().getNumber().toString();
	}

	return "-";
    }

    @Override
    public String getStudentName() {
	return event.getPerson().getName();
    }

    @Override
    public String getStudentEmail() {
	return event.getPerson().getDefaultEmailAddressValue();
    }

    @Override
    public String getRegistrationStartDate() {
	if (event.getPhdIndividualProgramProcess().getWhenFormalizedRegistration() != null) {
	    return event.getPhdIndividualProgramProcess().getWhenFormalizedRegistration().toString("dd/MM/yyyy");
	}

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
	return event.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(Language.pt);
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
	return Wrapper.PHD_PROGRAM_STUDIES;
    }

    @Override
    public String getTotalDiscount() {
	return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return !ExecutionYear.readByDateTime(event.getWhenOccured()).isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
	return ExecutionYear.readByDateTime(event.getWhenOccured());
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
	return event.getPhdIndividualProgramProcess().getAdministrativeOffice();
    }

}
