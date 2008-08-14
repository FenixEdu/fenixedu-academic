package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public class SearchPeopleDA extends FenixDispatchAction {

    public ActionForward search(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return mapping.findForward("search");
    }

    public ActionForward downloadActiveStudentList(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {

	final String filename = "Students_" + new DateTime().toString("yyyyMMddHHmm");
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=" + filename + ".tsv");

	final Spreadsheet spreadsheet = new Spreadsheet("ControloMetodosAvaliacao");
	downloadActiveStudentList(spreadsheet);

	final ServletOutputStream outputStream = response.getOutputStream();
	spreadsheet.exportToCSV(outputStream, "\t");
	outputStream.flush();
	response.flushBuffer();
	outputStream.close();

	return null;
    }

    private void downloadActiveStudentList(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Número Aluno");
	spreadsheet.setHeader("Nome Aluno");
	spreadsheet.setHeader("Tipo Curso");
	spreadsheet.setHeader("Nome Curso");
	spreadsheet.setHeader("Sigla Curso");

	for (final Student student : rootDomainObject.getStudentsSet()) {
	    final Collection<Registration> registrations = student.getActiveRegistrations();
	    if (!registrations.isEmpty()) {
		final Row row = spreadsheet.addRow();
		row.setCell(student.getNumber().toString());
		row.setCell(student.getPerson().getName());

		final StringBuilder degreeTypes = new StringBuilder();
		final StringBuilder degreeNames = new StringBuilder();
		final StringBuilder degreeAcronyms = new StringBuilder();

		for (final Registration registration : registrations) {
		    if (degreeTypes.length() > 0) {
			degreeTypes.append(", ");
			degreeNames.append(", ");
			degreeAcronyms.append(", ");
		    }
		    final Degree degree = registration.getDegree();
		    degreeTypes.append(degree.getDegreeType().getLocalizedName());
		    degreeNames.append(degree.getPresentationName());
		    degreeAcronyms.append(degree.getSigla());
		}

		row.setCell(degreeTypes.toString());
		row.setCell(degreeNames.toString());
		row.setCell(degreeAcronyms.toString());
	    }
	}
    }

}
