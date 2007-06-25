package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.util.search.StudentSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentEquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Student student = getStudent(request);
	if (student != null) {
	    request.setAttribute("student", student);

	    final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan = getStudentCurricularPlanEquivalencePlan(request, student);
	    if (studentCurricularPlanEquivalencePlan != null) {
		request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) request.getAttribute("degreeCurricularPlan");
		studentCurricularPlanEquivalencePlan.getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan);
		request.setAttribute("rootEquivalencyPlanEntryCurriculumModuleWrapper", studentCurricularPlanEquivalencePlan.getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan));
	    }
	}
	return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Student student = getStudent(request);
	if (student != null) {
	    request.setAttribute("student", student);

	    final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan = getStudentCurricularPlanEquivalencePlan(request, student);
	    if (studentCurricularPlanEquivalencePlan != null) {
		request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) request.getAttribute("degreeCurricularPlan");
		final CurriculumModule curriculumModule = getCurriculumModule(request);
		request.setAttribute("equivalencePlanEntryWrappers", studentCurricularPlanEquivalencePlan.getEquivalencePlanEntryWrappers(degreeCurricularPlan, curriculumModule));
//		request.setAttribute("equivalencePlanEntries", studentCurricularPlanEquivalencePlan.getEquivalencePlanEntries(degreeCurricularPlan, curriculumModule));
//		request.setAttribute("removedEquivalencePlanEntries", studentCurricularPlanEquivalencePlan.getRemovedEquivalencePlanEntries(degreeCurricularPlan, curriculumModule));
	    }
	}
	return mapping.findForward("showPlan");	
    }

    private StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan(final HttpServletRequest request, final Student student)
    		throws FenixFilterException, FenixServiceException {
	final Object[] args = { student };
	return (StudentCurricularPlanEquivalencePlan) executeService("CreateStudentCurricularPlanEquivalencePlan", args);
    }

    private Student getStudent(final HttpServletRequest request) {
	StudentSearchBean studentSearchBean = (StudentSearchBean) getRenderedObject(StudentSearchBean.class.getName());
	if (studentSearchBean == null) {
	    studentSearchBean = new StudentSearchBean();
	    final String studentNumber = request.getParameter("studentNumber");
	    if (studentNumber != null && studentNumber.length() > 0) {
		studentSearchBean.setStudentNumber(Integer.valueOf(studentNumber));
	    }
	}
	request.setAttribute("studentSearchBean", studentSearchBean);
	return studentSearchBean.search();
    }

    private CurriculumModule getCurriculumModule(final HttpServletRequest request) {
	final String curriculumModuleIDString = request.getParameter("curriculumModuleID");
	final Integer curriculumModuleID = getInteger(curriculumModuleIDString);
	return curriculumModuleID == null ? null : rootDomainObject.readCurriculumModuleByOID(curriculumModuleID);
    }
    
    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
	final String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
	final Integer degreeCurricularPlanID = getInteger(degreeCurricularPlanIDString);
	return degreeCurricularPlanID == null ? null : rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }

    private Integer getInteger(final String string) {
	return isValidNumber(string) ? Integer.valueOf(string) : null;
    }

    private boolean isValidNumber(final String string) {
	return string != null && string.length() > 0 && StringUtils.isNumeric(string);
    }

}
