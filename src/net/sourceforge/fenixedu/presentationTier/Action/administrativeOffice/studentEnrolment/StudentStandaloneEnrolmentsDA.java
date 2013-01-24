package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentStandaloneEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentStandaloneEnrolments", module = "academicAdministration")
@Forwards({
	@Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
	@Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
	@Forward(name = "showDegreeModulesToEnrol", path = "/studentEnrolments.do?method=prepareFromExtraEnrolment") })
public class StudentStandaloneEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentStandaloneEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	return new StudentStandaloneEnrolmentBean(studentCurricularPlan, executionSemester);
    }

    @Override
    protected String getActionName() {
	return "studentStandaloneEnrolments";
    }

    @Override
    protected NoCourseGroupCurriculumGroupType getGroupType() {
	return NoCourseGroupCurriculumGroupType.STANDALONE;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final ExecutionSemester semester = getExecutionSemester(request);
	if (isStudentInPartialRegime(request, semester)) {
	    addActionMessage("error", request, "error.Student.has.partial.regime", semester.getQualifiedName());
	}
	return super.prepare(mapping, actionForm, request, response);
    }

    private void chooseCurricular(HttpServletRequest request, CurricularRuleLevel level) {

	final StudentStandaloneEnrolmentBean bean = (StudentStandaloneEnrolmentBean) getNoCourseGroupEnrolmentBean();
	bean.setCurricularRuleLevel(level);

	request.setAttribute("enrolmentBean", bean);
	RenderUtils.invalidateViewState();
    }

    @Override
    public ActionForward chooseCurricular(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	chooseCurricular(request, CurricularRuleLevel.STANDALONE_ENROLMENT);
	return mapping.findForward("chooseExtraEnrolment");
    }

    public ActionForward chooseCurricularWithoutRules(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	chooseCurricular(request, CurricularRuleLevel.STANDALONE_ENROLMENT_NO_RULES);
	return mapping.findForward("chooseExtraEnrolment");
    }

    private boolean isStudentInPartialRegime(final HttpServletRequest request, final ExecutionSemester semester) {
	return getStudentCurricularPlan(request).getRegistration().isPartialRegime(semester.getExecutionYear());
    }

}
