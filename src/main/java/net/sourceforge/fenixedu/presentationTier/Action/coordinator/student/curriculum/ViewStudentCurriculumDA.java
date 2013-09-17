package net.sourceforge.fenixedu.presentationTier.Action.coordinator.student.curriculum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/viewStudentCurriculumSearch", attribute = "viewStudentCurriculumForm",
        formBean = "viewStudentCurriculumForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseStudent", path = "/coordinator/student/curriculum/chooseStudent.jsp", tileProperties = @Tile(
                title = "private.coordinator.management.courses.students.curriculum")),
        @Forward(name = "chooseCurriculumType", path = "/coordinator/student/curriculum/chooseCurriculumType.jsp") })
public class ViewStudentCurriculumDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        setDegreeCurricularPlanId(form, getDegreeCurricularPlanId(request));
        setExecutionDegreeId(form, getExecutionDegreeId(request));

        return mapping.findForward("chooseStudent");
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getStudent(actionForm);

        if (student == null) {
            addActionMessage(request, "label.coordinator.viewStudentCurriculum.noStudent");
            return mapping.findForward("chooseStudent");
        }

        if (!getDegreeCurricularPlan(actionForm).isBolonhaDegree()) {

            if (!student.hasTransitionRegistrations()) {
                return getOldCurriculumRedirect(actionForm, student);
            }

            request.setAttribute("student", student);

            return mapping.findForward("chooseCurriculumType");
        } else {
            return getOldCurriculumRedirect(actionForm, student);
        }

    }

    private ActionForward getBolonhaTransitionRedirect(final ActionForm actionForm, final Student student) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setPath("/bolonhaTransitionManagement.do?method=prepare&studentId=" + student.getExternalId());

        return actionForward;

    }

    private ActionForward getOldCurriculumRedirect(final ActionForm actionForm, final Student student) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setPath("/viewCurriculum.do?method=prepareReadByStudentNumber&studentNumber=" + student.getNumber()
                + "&executionDegreeId=" + getExecutionDegreeId(actionForm) + "&degreeCurricularPlanID="
                + getDegreeCurricularPlanId(actionForm));

        return actionForward;

    }

    private String getExecutionDegreeId(final HttpServletRequest request) {
        return request.getParameter("executionDegreeId");
    }

    private String getExecutionDegreeId(final ActionForm actionForm) {
        return (String) ((DynaActionForm) actionForm).get("executionDegreeId");
    }

    private void setExecutionDegreeId(final ActionForm actionForm, final String id) {
        ((DynaActionForm) actionForm).set("executionDegreeId", id);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final ActionForm form) {
        return FenixFramework.getDomainObject(getDegreeCurricularPlanId(form));
    }

    private String getDegreeCurricularPlanId(final HttpServletRequest request) {
        return request.getParameter("degreeCurricularPlanID");
    }

    private void setDegreeCurricularPlanId(final ActionForm actionForm, final String id) {
        ((DynaActionForm) actionForm).set("degreeCurricularPlanId", id);
    }

    private String getDegreeCurricularPlanId(final ActionForm actionForm) {
        return (String) ((DynaActionForm) actionForm).get("degreeCurricularPlanId");
    }

    private Student getStudent(final ActionForm actionForm) {
        final String studentNumberString = ((DynaActionForm) actionForm).getString("studentNumber");

        if (!StringUtils.isEmpty(studentNumberString) && NumberUtils.isNumber(studentNumberString)) {
            return Student.readStudentByNumber(Integer.valueOf(studentNumberString));
        }

        return null;
    }

}
