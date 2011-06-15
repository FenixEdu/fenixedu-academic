package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class GratuityEventWrapper implements Wrapper {
    private GratuityEvent event;

    public GratuityEventWrapper(final GratuityEvent event) {
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
    public String getRegistrationStartDate() {
	return event.getRegistration().getStartDate().toString("dd/MM/yyyy");
    }

    @Override
    public String getExecutionYear() {
	return event.getExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
	return event.getRegistration().getDegree().getNameI18N().getContent(Language.pt);
    }

    @Override
    public String getDegreeType() {
	return event.getRegistration().getDegreeType().getLocalizedName();
    }

    @Override
    public String getPhdProgramName() {
	return "-";
    }

    @Override
    public String getEnrolledECTS() {
	return new BigDecimal(event.getRegistration().getLastStudentCurricularPlan()
		.getEnrolmentsEctsCredits(event.getExecutionYear())).toString();
    }

    @Override
    public String getRegime() {
	return event.getRegistration().getRegimeType(event.getExecutionYear()).getLocalizedName();
    }

    @Override
    public String getEnrolmentModel() {
	if (event.isDfaGratuityEvent()) {
	    EnrolmentModel enrolmentModelForExecutionYear = event.getRegistration().getEnrolmentModelForExecutionYear(
		    event.getExecutionYear());
	    if (enrolmentModelForExecutionYear != null) {
		return enrolmentModelForExecutionYear.getLocalizedName();
	    }
	}

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
	return REGISTRATION_STUDIES;
    }

    @Override
    public String getTotalDiscount() {
	return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return !event.getExecutionYear().isBefore(executionYear);
    }

}
