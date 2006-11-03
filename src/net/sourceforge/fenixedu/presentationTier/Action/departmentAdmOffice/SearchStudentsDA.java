package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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

}
