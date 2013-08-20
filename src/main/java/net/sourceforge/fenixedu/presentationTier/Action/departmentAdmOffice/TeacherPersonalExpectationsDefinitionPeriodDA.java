package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "departmentAdmOffice", path = "/teacherPersonalExpectationsDefinitionPeriod", scope = "request",
        parameter = "method")
@Forwards(
        value = {
                @Forward(
                        name = "showDefinitionPeriod",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showTeacherPersonalExpectationsDefinitionPeriod.jsp"),
                @Forward(
                        name = "editDefinitionPeriod",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editTeacherPersonalExpectationsDefinitionPeriod.jsp") })
public class TeacherPersonalExpectationsDefinitionPeriodDA extends FenixDispatchAction {

    public ActionForward showPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear year = ExecutionYear.readCurrentExecutionYear();
        readAndSetPeriod(request, year);
        request.setAttribute("bean", new ExecutionYearBean(year));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward showPeriodWithSelectedYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();
        readAndSetPeriod(request, executionYear);
        request.setAttribute("bean", new ExecutionYearBean(executionYear));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward showPeriodInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        readAndSetPeriod(request, executionYear);
        request.setAttribute("bean", new ExecutionYearBean(executionYear));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward createPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("department", getDepartment(request));
        return mapping.findForward("editDefinitionPeriod");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        readAndSetPeriod(request, executionYear);
        return mapping.findForward("editDefinitionPeriod");
    }

    // Private Methods

    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear executionYear) {
        if (executionYear != null) {
            TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod =
                    getDepartment(request).getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
        }
    }

    protected ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearId");
        return AbstractDomainObject.fromExternalId(executionYearIDString);
    }

    protected Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }
}
