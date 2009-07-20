package net.sourceforge.fenixedu.domain.reports;

import java.util.Collections;
import java.util.LinkedList;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class FlunkedReportFile extends FlunkedReportFile_Base {

    public FlunkedReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de prescrições";
    }

    @Override
    protected String getPrefix() {
	return "prescricoes";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("número aluno");
	setDegreeHeaders(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		for (final Registration registration : degree.getRegistrationsSet()) {
		    LinkedList<RegistrationState> states = new LinkedList<RegistrationState>();
		    states.addAll(registration.getRegistrationStates());
		    Collections.sort(states, RegistrationState.DATE_COMPARATOR);
		    CollectionUtils.filter(states, new Predicate() {
			@Override
			public boolean evaluate(Object item) {
			    return ((RegistrationState) item).getExecutionYear() != null
				    && ((RegistrationState) item).getExecutionYear().isBeforeOrEquals(getExecutionYear());
			}
		    });
		    if (!states.isEmpty() && states.getLast().getStateType().equals(RegistrationStateType.FLUNKED)) {
			final Row row = spreadsheet.addRow();
			row.setCell(registration.getNumber());
			setDegreeCells(row, degree);
		    }
		}
	    }
	}
    }

}
