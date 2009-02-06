package net.sourceforge.fenixedu.domain.reports;

import java.util.HashMap;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.gep.ReportsByDegreeTypeDA;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class StatusAndApprovalReportFile extends StatusAndApprovalReportFile_Base {

    StatusAndApprovalReportFile() {
	super();
    }

    public String getJobName() {
	return "Estatuto e aprovações entre " + getExecutionYear().getYear() + " e "
		+ getExecutionYearFourYearsBack(getExecutionYear()).getYear();
    }

    public static class EnrolmentAndAprovalCounter {
	private int enrolments = 0;
	private int aprovals = 0;

	public void count(final Enrolment enrolment) {
	    enrolments++;
	    if (enrolment.isApproved()) {
		aprovals++;
	    }
	}

	public int getEnrolments() {
	    return enrolments;
	}

	public int getAprovals() {
	    return aprovals;
	}
    }

    public static class EnrolmentAndAprovalCounterMap extends HashMap<ExecutionSemester, EnrolmentAndAprovalCounter> {

	private final ExecutionSemester firstExecutionSemester;
	private final ExecutionSemester lastExecutionSemester;

	public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
		final ExecutionSemester lastExecutionSemester) {
	    this.firstExecutionSemester = firstExecutionSemester;
	    this.lastExecutionSemester = lastExecutionSemester;
	}

	public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
		final ExecutionSemester lastExecutionSemester, final Registration registration) {
	    this(firstExecutionSemester, lastExecutionSemester);
	    for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
		if (otherRegistration.getDegree() == registration.getDegree()) {
		    for (final StudentCurricularPlan studentCurricularPlan : otherRegistration.getStudentCurricularPlansSet()) {
			for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
			    count(enrolment);
			}
		    }
		}
	    }
	}

	public void count(final Enrolment enrolment) {
	    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	    if (firstExecutionSemester.isBeforeOrEquals(executionSemester)
		    && executionSemester.isBeforeOrEquals(lastExecutionSemester)) {
		final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = get(executionSemester);
		enrolmentAndAprovalCounter.count(enrolment);
	    }
	}

	@Override
	public EnrolmentAndAprovalCounter get(final Object key) {
	    EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = super.get(key);
	    if (enrolmentAndAprovalCounter == null) {
		enrolmentAndAprovalCounter = new EnrolmentAndAprovalCounter();
		put((ExecutionSemester) key, enrolmentAndAprovalCounter);
	    }
	    return enrolmentAndAprovalCounter;
	}

    }

    protected String getPrefix() {
	return "statusAndAproval";
    }

    public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
	ExecutionYear executionYearFourYearsBack = executionYear;
	if (executionYear != null) {
	    for (int i = 5; i > 1; i--) {
		final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
		if (previousExecutionYear != null) {
		    executionYearFourYearsBack = previousExecutionYear;
		}
	    }
	}
	return executionYearFourYearsBack;
    }

    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("nï¿½mero aluno");
	spreadsheet.setHeader("ano lectivo");
	spreadsheet.setHeader("semestre");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("estatuto");
	spreadsheet.setHeader("nï¿½mero inscricoes");
	spreadsheet.setHeader("nï¿½umero aprovacoes");

	final ExecutionSemester firstExecutionSemester = ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(getExecutionYear())
		.getFirstExecutionPeriod();
	final ExecutionSemester lastExecutionSemester = getExecutionYear().getLastExecutionPeriod();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		if (isActive(degree)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(getExecutionYear())) {
			    final EnrolmentAndAprovalCounterMap map = new EnrolmentAndAprovalCounterMap(firstExecutionSemester,
				    lastExecutionSemester, registration);
			    for (final Entry<ExecutionSemester, EnrolmentAndAprovalCounter> entry : map.entrySet()) {
				final ExecutionSemester executionSemester = entry.getKey();
				final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = entry.getValue();

				final Row row = spreadsheet.addRow();
				row.setCell(registration.getNumber().toString());
				row.setCell(executionSemester.getExecutionYear().getYear());
				row.setCell(executionSemester.getSemester().toString());
				setDegreeCells(row, degree);
				final StringBuilder stringBuilder = new StringBuilder();
				for (final StudentStatuteBean studentStatuteBean : registration.getStudent().getStatutes(
					executionSemester)) {
				    if (stringBuilder.length() > 0) {
					stringBuilder.append(", ");
				    }
				    stringBuilder.append(studentStatuteBean.getStudentStatute().getStatuteType());
				}
				row.setCell(stringBuilder.toString());
				row.setCell(Integer.toString(enrolmentAndAprovalCounter.getEnrolments()));
				row.setCell(Integer.toString(enrolmentAndAprovalCounter.getAprovals()));
			    }
			}
		    }
		}
	    }
	}
    }
}
