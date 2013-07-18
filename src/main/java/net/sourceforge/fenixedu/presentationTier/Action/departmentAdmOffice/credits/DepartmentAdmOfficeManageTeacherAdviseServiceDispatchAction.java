/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherAdviseServiceDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(module = "departmentAdmOffice", path = "/teacherAdviseServiceManagement",
        input = "/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0",
        attribute = "teacherDegreeFinalProjectStudentForm", formBean = "teacherDegreeFinalProjectStudentForm", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "list-teacher-advise-services", path = "list-teacher-advise-services"),
        @Forward(name = "successfull-delete", path = "/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0"),
        @Forward(name = "successfull-edit", path = "/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0"),
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException.class,
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixExceptionMessageHandler.class,
                        scope = "request"),
                @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class,
                        scope = "request") })
public class DepartmentAdmOfficeManageTeacherAdviseServiceDispatchAction extends ManageTeacherAdviseServiceDispatchAction {

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        IUserView userView = UserView.getUser();

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

        Teacher teacher = AbstractDomainObject.fromExternalId(dynaForm.getString("teacherId"));
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();

        if (teacher == null || teacher.getCurrentWorkingDepartment() == null
                || !manageableDepartments.contains(teacher.getCurrentWorkingDepartment())) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        getAdviseServices(request, dynaForm, executionSemester, teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {

        return editAdviseService(form, request, mapping, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {

        deleteAdviseService(request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");

    }
}
