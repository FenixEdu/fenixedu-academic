package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class FlunkedReportFile extends FlunkedReportFile_Base {

    FlunkedReportFile() {
	super();
    }

    public String getJobName() {
	return "Listagem de prescrições";
    }

    protected String getPrefix() {
	return "prescricoes";
    }

    public void renderReport(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("nï¿½mero aluno");
	setDegreeHeaders(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		for (final Registration registration : degree.getRegistrationsSet()) {
		    for (final RegistrationState registrationState : registration.getRegistrationStates()) {
			final RegistrationStateType registrationStateType = registrationState.getStateType();
			if (registrationStateType == RegistrationStateType.FLUNKED
				&& registrationState.getExecutionYear() == getExecutionYear()) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);
			}
		    }
		}
	    }
	}
    }

}
