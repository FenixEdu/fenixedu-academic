/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.io.OutputStream;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeStudentsApp;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = DepartmentAdmOfficeStudentsApp.class, path = "search-students", titleKey = "link.students.search")
@Mapping(module = "departmentAdmOffice", path = "/searchStudents")
@Forwards(@Forward(name = "searchStudents", path = "/departmentAdmOffice/searchStudents.jsp"))
public class SearchStudentsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SearchStudentsWithEnrolmentsByDepartment searchStudentsWithEnrolmentsByDepartment = getRenderedObject();
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

    public ActionForward download(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=students.xls");

        final SearchStudentsWithEnrolmentsByDepartment searchStudentsWithEnrolmentsByDepartment = getRenderedObject();
        final Set<StudentCurricularPlan> studentCurricularPlans = searchStudentsWithEnrolmentsByDepartment.search();
        final ExecutionYear executionYear = searchStudentsWithEnrolmentsByDepartment.getExecutionYear();
        final Spreadsheet spreadsheet = getSpreadsheet(executionYear);
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
            row.setCell(Integer.toString(registration.getCurricularYear(executionYear)));
        }
        final OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.close();
        return null;
    }

    private Spreadsheet getSpreadsheet(final ExecutionYear executionYear) {
        final Spreadsheet spreadsheet =
                new Spreadsheet(BundleUtil.getString(Bundle.APPLICATION, "label.student.for.academic.year") + " "
                        + executionYear.getYear().replace('/', ' '));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.degree.code"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.student.number"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.person.name"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.person.email"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.student.curricular.year"));
        return spreadsheet;
    }

}
