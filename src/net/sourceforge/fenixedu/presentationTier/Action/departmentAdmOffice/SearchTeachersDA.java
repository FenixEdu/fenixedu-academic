package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchTeachersDA extends FenixDispatchAction {

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    public ActionForward download(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=teachers.xls");

        final Department department = getDepartment(request);

        final Spreadsheet spreadsheet = getSpreadsheet();
        for (final Teacher teacher : department.getAllCurrentTeachers()) {
            final Person person = teacher.getPerson();

            final Row row = spreadsheet.addRow();
            row.setCell(teacher.getTeacherNumber().toString());
            row.setCell(person.getName());
            row.setCell(person.getEmail());
        }

        final OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();
        return null;
    }

    private Spreadsheet getSpreadsheet() {
	final ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", new Locale("pt", "PT"));
	final Spreadsheet spreadsheet = new Spreadsheet("Teachers");
	spreadsheet.setHeader(enumResourceBundle.getString("label.teacher.number"));
	spreadsheet.setHeader(enumResourceBundle.getString("label.person.name"));
	spreadsheet.setHeader(enumResourceBundle.getString("label.person.email"));
	return spreadsheet;
    }

}
