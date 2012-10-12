package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ReportStudentsUTLCandidatesForOneStudent extends ReportStudentsUTLCandidates {

    public ReportStudentsUTLCandidatesForOneStudent(final ExecutionYear forExecutionYear, final Student student) {
	super(forExecutionYear);

	getStudentLines(student);
    }

    protected void getStudentLines(HSSFSheet sheet) {
	throw new RuntimeException();
    }

    protected void getStudentLines(final Student student) {
	StudentLine studentLine = new StudentLine();
	boolean filledWithSuccess = studentLine.fillWithStudent(forExecutionYear, student);

	if (filledWithSuccess) {
	    correctStudentLines.add(studentLine);
	} else {
	    erroneousStudentLines.add(studentLine);
	}
    }

}
