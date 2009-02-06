package net.sourceforge.fenixedu.domain.reports;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class RegistrationReportFile extends RegistrationReportFile_Base {

    RegistrationReportFile() {
	super();
    }

    public String getJobName() {
	return "Listagem de matrí­culas";
    }

    protected String getPrefix() {
	return "matriculas";
    }

    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("nï¿½mero aluno");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("cï¿½digo regime de ingresso na matrï¿½cula");
	spreadsheet.setHeader("regime de ingresso na matrï¿½cula");
	spreadsheet.setHeader("ano lï¿½ctivo de inï¿½cio da matrï¿½cula");
	spreadsheet.setHeader("cï¿½digo regime de ingresso na escola");
	spreadsheet.setHeader("regime de ingresso na escola");
	spreadsheet.setHeader("ano lï¿½ctivo de ingresso na escola");
	spreadsheet.setHeader("tipo de aluno");
	spreadsheet.setHeader("ano curricular");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		if (isActive(degree)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(getExecutionYear())) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);

			    reportIngression(row, registration);

			    final Registration firstRegistration = findFirstRegistration(registration.getStudent());
			    reportIngression(row, firstRegistration);

			    if (registration.getRegistrationAgreement() != null) {
				row.setCell(registration.getRegistrationAgreement().getName());
			    } else {
				row.setCell("");
			    }
			    row.setCell(Integer.toString(registration.getCurricularYear(getExecutionYear())));
			}
		    }
		}
	    }
	}
    }

    private Registration findFirstRegistration(final Student student) {
	return Collections.min(student.getRegistrationsSet(), Registration.COMPARATOR_BY_START_DATE);
    }

    private void reportIngression(final Row row, final Registration registration) {
	final Ingression ingression = registration.getIngression();
	if (ingression != null) {
	    row.setCell(ingression.getName());
	    row.setCell(ingression.getDescription());
	} else {
	    row.setCell("");
	    row.setCell("");
	}
	row.setCell(registration.getStartExecutionYear().getYear());
    }

}
