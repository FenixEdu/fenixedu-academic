package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchStudentsDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchStudentsWithEnrolmentsByDepartment searchStudentsWithEnrolmentsByDepartment = (SearchStudentsWithEnrolmentsByDepartment) getRenderedObject();
        if (searchStudentsWithEnrolmentsByDepartment == null) {
            final Department department = getDepartment(request);
            searchStudentsWithEnrolmentsByDepartment = new SearchStudentsWithEnrolmentsByDepartment(department);
        }
        request.setAttribute("searchStudentsWithEnrolmentsByDepartment", searchStudentsWithEnrolmentsByDepartment);
        return mapping.findForward("searchStudents");
    }

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    public ActionForward download(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=students.xls");

        final SearchStudentsWithEnrolmentsByDepartment searchStudentsWithEnrolmentsByDepartment = (SearchStudentsWithEnrolmentsByDepartment) getRenderedObject();
        final Spreadsheet spreadsheet = getSpreadsheet();
        final Set<StudentCurricularPlan> studentCurricularPlans = searchStudentsWithEnrolmentsByDepartment.search();
        for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
            final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            final Registration registration = studentCurricularPlan.getStudent();
            final Student student = registration.getStudent();
            final Person person = student.getPerson();

            final Row row = spreadsheet.addRow();
            row.setCell(degree.getSigla());
            row.setCell(student.getNumber().toString());
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
	final Spreadsheet spreadsheet = new Spreadsheet("Students");
	spreadsheet.setHeader(enumResourceBundle.getString("label.degree.code"));
	spreadsheet.setHeader(enumResourceBundle.getString("label.student.number"));
	spreadsheet.setHeader(enumResourceBundle.getString("label.person.name"));
	spreadsheet.setHeader(enumResourceBundle.getString("label.person.email"));
	return spreadsheet;
    }

}
