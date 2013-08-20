package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.CreateExpectationEvaluationGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.DeleteExpectationEvaluationGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.ExpectationEvaluationGroupBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

@Mapping(module = "departmentAdmOffice", path = "/defineExpectationEvaluationGroups", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(
                        name = "manageGroups",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/manageExpectationEvaluationGroups.jsp"),
                @Forward(
                        name = "listGroups",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/seeExpectationEvaluationGroups.jsp") })
public class ExpectationsEvaluationGroupsDA extends FenixDispatchAction {

    public ActionForward listGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Department department = getDepartment(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        readAndSetAppraiserTeachers(request, department, executionYear);
        return mapping.findForward("listGroups");
    }

    public ActionForward listGroupsWithSelectedExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Department department = getDepartment(request);
        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();
        readAndSetAppraiserTeachers(request, department, executionYear);
        return mapping.findForward("listGroups");
    }

    public ActionForward listGroupsInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Department department = getDepartment(request);
        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        readAndSetAppraiserTeachers(request, department, executionYear);
        return mapping.findForward("listGroups");
    }

    public ActionForward manageGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getTeacherFromParameter(request);
        ExecutionYear executionYear = getExecutionYearFromParameter(request);

        Department employeeDepartment = getDepartment(request);
        Department teacherWorkingDepartment =
                teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());

        if (teacherWorkingDepartment != null && employeeDepartment != null && teacherWorkingDepartment.equals(employeeDepartment)) {
            request.setAttribute("expectationEvaluationGroupBean", new ExpectationEvaluationGroupBean(teacher, executionYear));
            request.setAttribute("evaluatedTeacherGroups", teacher.getEvaluatedExpectationEvaluationGroups(executionYear));
        }

        return mapping.findForward("manageGroups");
    }

    public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("expectationEvaluationGroupBeanWithEvaluatedTeacher");
        ExpectationEvaluationGroupBean bean = (ExpectationEvaluationGroupBean) viewState.getMetaObject().getObject();

        try {
            CreateExpectationEvaluationGroup.run(bean.getAppraiser(), bean.getEvaluated(), bean.getExecutionYear());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        RenderUtils.invalidateViewState("expectationEvaluationGroupBeanWithEvaluatedTeacher");
        bean.setEvaluated(null);
        request.setAttribute("evaluatedTeacherGroups",
                bean.getAppraiser().getEvaluatedExpectationEvaluationGroups(bean.getExecutionYear()));
        request.setAttribute("expectationEvaluationGroupBean", bean);

        return mapping.findForward("manageGroups");
    }

    public ActionForward deleteGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ExpectationEvaluationGroup group = getExpectationEvaluationGroupFromParameter(request);
        Teacher appraiser = group.getAppraiser();
        ExecutionYear executionYear = group.getExecutionYear();

        Department employeeDepartment = getDepartment(request);
        Department appraiserDepartment =
                appraiser.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());

        if (appraiserDepartment != null && employeeDepartment != null && appraiserDepartment.equals(employeeDepartment)) {
            try {
                DeleteExpectationEvaluationGroup.run(group);
                request.setAttribute("evaluatedTeacherGroups", appraiser.getEvaluatedExpectationEvaluationGroups(executionYear));
                request.setAttribute("expectationEvaluationGroupBean", new ExpectationEvaluationGroupBean(appraiser,
                        executionYear));

            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return mapping.findForward("manageGroups");
    }

    // private methods

    private void readAndSetAppraiserTeachers(HttpServletRequest request, Department department, ExecutionYear executionYear) {
        Map<Teacher, List<ExpectationEvaluationGroup>> result =
                new TreeMap<Teacher, List<ExpectationEvaluationGroup>>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
        if (executionYear != null && department != null) {
            List<Teacher> currentTeachers =
                    department.getAllTeachers(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());
            for (Teacher teacher : currentTeachers) {
                result.put(teacher, teacher.getEvaluatedExpectationEvaluationGroups(executionYear));
            }
        }
        request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
        request.setAttribute("teachers", result);
    }

    private ExpectationEvaluationGroup getExpectationEvaluationGroupFromParameter(final HttpServletRequest request) {
        final String goupIDString = request.getParameter("groupID");
        return AbstractDomainObject.fromExternalId(goupIDString);
    }

    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        return AbstractDomainObject.fromExternalId(executionYearIDString);
    }

    private Teacher getTeacherFromParameter(final HttpServletRequest request) {
        final String teacherIDString = request.getParameter("teacherID");
        return AbstractDomainObject.fromExternalId(teacherIDString);
    }

    private Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }
}