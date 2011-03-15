/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DelegateInquiryBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewCourseInquiryPublicResults;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ViewTeacherInquiryPublicResults;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/delegateInquiry", module = "delegate")
@Forwards( { @Forward(name = "chooseCoursesToAnswer", path = "/delegate/inquiries/chooseCoursesToAnswer.jsp"),
	@Forward(name = "inquiry1stPage", path = "/delegate/inquiries/inquiry1stPage.jsp"),
	@Forward(name = "delegateInquiry", path = "/delegate/inquiries/delegateInquiry.jsp"),
	@Forward(name = "inquiriesClosed", path = "/delegate/inquiries/inquiriesClosed.jsp") })
public class YearDelegateInquiryDA extends FenixDispatchAction {

    public ActionForward showCoursesToAnswerPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final DelegateInquiryTemplate delegateInquiryTemplate = DelegateInquiryTemplate.getCurrentTemplate();
	if (delegateInquiryTemplate == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}
	YearDelegate yearDelegate = null;
	ExecutionSemester executionPeriod = delegateInquiryTemplate.getExecutionPeriod();
	for (Delegate delegate : AccessControl.getPerson().getStudent().getDelegates()) {
	    if (delegate instanceof YearDelegate) {
		if (delegate.isActiveForFirstExecutionYear(executionPeriod.getExecutionYear())) {
		    if (yearDelegate == null
			    || delegate.getDelegateFunction().getEndDate().isAfter(
				    yearDelegate.getDelegateFunction().getEndDate())) {
			yearDelegate = (YearDelegate) delegate;
		    }
		}
	    }
	}

	if (yearDelegate != null) {
	    Degree degree = yearDelegate.getRegistration().getDegree();
	    CycleType currentCycleType = yearDelegate.getRegistration().getCurrentCycleType();
	    FunctionType functionType = getFunctionType(degree);
	    Student student = yearDelegate.getRegistration().getStudent();
	    PersonFunction degreeDelegateFunction = degree.getActiveDelegatePersonFunctionByStudentAndFunctionType(student,
		    executionPeriod.getExecutionYear(), functionType);

	    ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(yearDelegate
		    .getRegistration().getStudentCurricularPlan(executionPeriod).getDegreeCurricularPlan(), executionPeriod
		    .getExecutionYear());
	    Set<ExecutionCourse> executionCoursesToInquiries = yearDelegate.getExecutionCoursesToInquiries(executionPeriod,
		    executionDegree);
	    if (degreeDelegateFunction != null) {
		addExecutionCoursesForOtherYears(yearDelegate, executionPeriod, executionDegree, degree, student,
			executionCoursesToInquiries);
	    }

	    List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
	    for (ExecutionCourse executionCourse : executionCoursesToInquiries) {
		coursesResultResume.add(new CurricularCourseResumeResult(executionCourse, executionDegree, yearDelegate));
	    }
	    Collections.sort(coursesResultResume, new BeanComparator("executionCourse.name"));
	    request.setAttribute("executionDegree", executionDegree);
	    request.setAttribute("executionPeriod", executionPeriod);
	    request.setAttribute("coursesResultResume", coursesResultResume);
	    return actionMapping.findForward("chooseCoursesToAnswer");
	}

	return actionMapping.findForward("inquiriesClosed");
    }

    private void addExecutionCoursesForOtherYears(YearDelegate yearDelegate, ExecutionSemester executionPeriod,
	    ExecutionDegree executionDegree, Degree degree, Student student, Set<ExecutionCourse> executionCoursesToInquiries) {
	List<YearDelegate> otherYearDelegates = new ArrayList<YearDelegate>();
	for (Student forStudent : degree.getAllActiveYearDelegates()) {
	    if (forStudent != student) {
		YearDelegate otherYearDelegate = null;
		for (Delegate delegate : forStudent.getDelegates()) {
		    if (delegate instanceof YearDelegate) {
			if (delegate.isActiveForFirstExecutionYear(executionPeriod.getExecutionYear())) {
			    if (otherYearDelegate == null
				    || delegate.getDelegateFunction().getEndDate().isAfter(
					    otherYearDelegate.getDelegateFunction().getEndDate())) {
				otherYearDelegate = (YearDelegate) delegate;
			    }
			}
		    }
		}
		if (otherYearDelegate != null) {
		    otherYearDelegates.add(otherYearDelegate);
		}
	    }
	}
	for (int iter = 1; iter < degree.getDegreeType().getYears(); iter++) {
	    YearDelegate yearDelegateForYear = getYearDelegate(otherYearDelegates, iter);
	    if (yearDelegateForYear == null) {
		executionCoursesToInquiries.addAll(yearDelegate.getExecutionCoursesToInquiries(executionPeriod, executionDegree,
			iter));
	    }
	}
    }

    private FunctionType getFunctionType(Degree degree) {
	switch (degree.getDegreeType()) {
	case BOLONHA_DEGREE:
	    return FunctionType.DELEGATE_OF_DEGREE;
	case BOLONHA_MASTER_DEGREE:
	    return FunctionType.DELEGATE_OF_MASTER_DEGREE;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    degree.getDegreeType().getYears(CycleType.FIRST_CYCLE);
	    degree.getDegreeType().getYears(CycleType.SECOND_CYCLE);
	    return FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE;
	default:
	    return null;
	}
    }

    private YearDelegate getYearDelegate(List<YearDelegate> otherYearDelegates, int year) {
	for (YearDelegate yearDelegate : otherYearDelegates) {
	    if (yearDelegate.getCurricularYear().getYear() == year) {
		return yearDelegate;
	    }
	}
	return null;
    }

    public ActionForward showFillInquiryPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	YearDelegate yearDelegate = AbstractDomainObject.fromExternalId(getFromRequest(request, "yearDelegateOID").toString());
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID")
		.toString());
	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());

	List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
	DelegateInquiryTemplate delegateInquiryTemplate = DelegateInquiryTemplate.getCurrentTemplate();
	InquiryDelegateAnswer inquiryDelegateAnswer = null;
	for (InquiryDelegateAnswer delegateAnswer : yearDelegate.getInquiryDelegateAnswers()) {
	    if (delegateAnswer.getExecutionCourse() == executionCourse) {
		inquiryDelegateAnswer = delegateAnswer;
	    }
	}

	DelegateInquiryBean delegateInquiryBean = new DelegateInquiryBean(executionCourse, delegateInquiryTemplate, results,
		yearDelegate, inquiryDelegateAnswer);

	request.setAttribute("hasNotRelevantData", executionCourse.hasNotRelevantDataFor(executionDegree));
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("executionDegree", executionDegree);
	request.setAttribute("delegateInquiryBean", delegateInquiryBean);

	return actionMapping.findForward("delegateInquiry");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final DelegateInquiryBean delegateInquiryBean = getRenderedObject("delegateInquiryBean");
	if (!delegateInquiryBean.isValid()) {
	    request.setAttribute("delegateInquiryBean", delegateInquiryBean);
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("delegateInquiry");
	}
	RenderUtils.invalidateViewState("delegateInquiryBean");
	delegateInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.DELEGATE);

	return showCoursesToAnswerPage(actionMapping, actionForm, request, response);
    }

    public ActionForward viewCourseInquiryResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return ViewCourseInquiryPublicResults.getCourseResultsActionForward(mapping, form, request, response);
    }

    public ActionForward viewTeacherShiftTypeInquiryResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return ViewTeacherInquiryPublicResults.getTeacherResultsActionForward(mapping, form, request, response);
    }
}
