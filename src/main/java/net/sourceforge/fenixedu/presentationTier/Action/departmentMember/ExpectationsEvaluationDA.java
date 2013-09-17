package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/evaluateExpectations", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "chooseTeacher",
                        path = "/departmentMember/expectationManagement/listTeachersToEvaluateExpectation.jsp"),
                @Forward(name = "seeTeacherPersonalExpectations",
                        path = "/departmentMember/expectationManagement/seeTeacherPersonalExpectationsToEvaluate.jsp"),
                @Forward(name = "prepareEditEvaluation",
                        path = "/departmentMember/expectationManagement/editExpectationEvaluation.jsp") })
public class ExpectationsEvaluationDA extends FenixDispatchAction {

    public ActionForward chooseTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);

        return mapping.findForward("chooseTeacher");
    }

    public ActionForward chooseTeacherInSelectedExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();

        readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);

        return mapping.findForward("chooseTeacher");
    }

    public ActionForward chooseTeacherInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        ExecutionYear executionYear = getExecutionYearFromParameter(request);

        readAndSetEvaluatedTeachersWithExpectations(request, teacher, executionYear);

        return mapping.findForward("chooseTeacher");
    }

    public ActionForward prepareEditExpectationEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TeacherPersonalExpectation expectation = getTeacherPersonalExpectationFromParameter(request);
        if (expectation != null) {
            Teacher teacher = getLoggedTeacher(request);
            ExecutionYear executionYear = expectation.getExecutionYear();
            if (teacher.hasExpectationEvaluatedTeacher(expectation.getTeacher(), executionYear)) {
                request.setAttribute("teacherPersonalExpectation", expectation);
            }
        }
        return mapping.findForward("prepareEditEvaluation");
    }

    public ActionForward seeTeacherPersonalExpectation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        Teacher loggedTeacher = getLoggedTeacher(request);
        if (teacherPersonalExpectation != null
                && loggedTeacher.hasExpectationEvaluatedTeacher(teacherPersonalExpectation.getTeacher(),
                        teacherPersonalExpectation.getExecutionYear())) {
            request.setAttribute("noEdit", true);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }
        return mapping.findForward("seeTeacherPersonalExpectations");
    }

    private TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
        final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
        return FenixFramework.getDomainObject(teacherPersonalExpectationIDString);
    }

    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        return FenixFramework.getDomainObject(executionYearIDString);
    }

    private void readAndSetEvaluatedTeachersWithExpectations(HttpServletRequest request, Teacher teacher,
            ExecutionYear executionYear) {
        Map<Teacher, TeacherPersonalExpectation> result =
                new TreeMap<Teacher, TeacherPersonalExpectation>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
        List<ExpectationEvaluationGroup> groups = teacher.getEvaluatedExpectationEvaluationGroups(executionYear);
        for (ExpectationEvaluationGroup group : groups) {
            TeacherPersonalExpectation evaluatedTeacherExpectation =
                    group.getEvaluated().getTeacherPersonalExpectationByExecutionYear(executionYear);
            result.put(group.getEvaluated(), evaluatedTeacherExpectation);
        }
        request.setAttribute("evaluatedTeachers", result);
        request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        Person loggedPerson = getLoggedPerson(request);
        return (loggedPerson != null) ? loggedPerson.getTeacher() : null;
    }
}
