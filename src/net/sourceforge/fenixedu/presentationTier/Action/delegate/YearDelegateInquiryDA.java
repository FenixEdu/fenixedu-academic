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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.student.Delegate;
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
	    final ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(yearDelegate
		    .getRegistration().getStudentCurricularPlan(executionPeriod).getDegreeCurricularPlan(), executionPeriod
		    .getExecutionYear());
	    Set<ExecutionCourse> executionCoursesToInquiries = yearDelegate.getExecutionCoursesToInquiries(executionPeriod,
		    executionDegree);

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

	DelegateInquiryBean delegateInquiryBean = new DelegateInquiryBean(executionCourse, executionDegree,
		delegateInquiryTemplate, results, yearDelegate, inquiryDelegateAnswer);

	request.setAttribute("hasNotRelevantData", executionCourse.hasNotRelevantDataFor(executionDegree));
	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("delegateInquiryBean", delegateInquiryBean);

	return actionMapping.findForward("delegateInquiry");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final DelegateInquiryBean delegateInquiryBean = getRenderedObject("delegateInquiryBean");
	if (delegateInquiryBean.getInquiryDelegateAnswer() == null) {
	    InquiryDelegateAnswer inquiryDelegateAnswer = null;
	    for (InquiryDelegateAnswer delegateAnswer : delegateInquiryBean.getYearDelegate().getInquiryDelegateAnswers()) {
		if (delegateAnswer.getExecutionCourse() == delegateInquiryBean.getExecutionCourse()) {
		    inquiryDelegateAnswer = delegateAnswer;
		}
	    }
	    delegateInquiryBean.setInquiryDelegateAnswer(inquiryDelegateAnswer);
	}
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
