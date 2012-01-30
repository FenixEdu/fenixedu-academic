package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.student.Student;

public class FctScholarshipPhdGratuityContribuitionEventWrapper implements Wrapper {
    private FctScholarshipPhdGratuityContribuitionEvent event;

    public FctScholarshipPhdGratuityContribuitionEventWrapper(Event event) {
	this.event = (FctScholarshipPhdGratuityContribuitionEvent) event;
    }

    private PhdGratuityEvent getPhdGratuityEvent() {
	PhdGratuityEvent phdGratuityEvent = (PhdGratuityEvent) this.event.getPhdGratuityFctScholarshipExemption().getEvent();

	return phdGratuityEvent;
    }

    private Student getStudent() {
	return getPhdGratuityEvent().getPerson().getStudent();
    }
    @Override
    public String getStudentNumber() {
	Student student = getStudent();

	return student.getNumber().toString();
    }

    @Override
    public String getStudentName() {
	return getStudent().getPerson().getName();
    }

    @Override
    public String getRegistrationStartDate() {
	return "";
    }

    @Override
    public String getExecutionYear() {
	return getForExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
	return "";
    }

    @Override
    public String getDegreeType() {
	return "";
    }

    @Override
    public String getPhdProgramName() {
	PhdGratuityEvent phdGratuityEvent = getPhdGratuityEvent();

	return phdGratuityEvent.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent();
    }

    @Override
    public String getEnrolledECTS() {
	return "";
    }

    @Override
    public String getRegime() {
	return "";
    }

    @Override
    public String getEnrolmentModel() {
	return "";
    }

    @Override
    public String getResidenceYear() {
	return "";
    }

    @Override
    public String getResidenceMonth() {
	return "";
    }

    @Override
    public String getStudiesType() {
	return Wrapper.PHD_PROGRAM_STUDIES;
    }

    @Override
    public String getTotalDiscount() {
	return "";
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
	return getForExecutionYear().isAfterOrEquals(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
	return ExecutionYear.readByDateTime(event.getEventStateDate());
    }

    @Override
    public AdministrativeOfficeType getRelatedAcademicOfficeType() {
	return AdministrativeOfficeType.MASTER_DEGREE;
    }

}
