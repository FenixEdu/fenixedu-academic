/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.YearDelegateCourseInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.inquiries.teacher.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/delegateInquiry", module = "delegate")
@Forwards( { @Forward(name = "chooseCoursesToAnswer", path = "/delegate/firstYearInquiry/chooseCoursesToAnswer.jsp"),
	@Forward(name = "inquiry1stPage", path = "/delegate/firstYearInquiry/inquiry1stPage.jsp"),
	@Forward(name = "inquiriesClosed", path = "/delegate/firstYearInquiry/inquiriesClosed.jsp") })
public class YearDelegateInquiryDA extends FenixDispatchAction {

    public ActionForward showCoursesToAnswerPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	InquiryResponsePeriod openPeriod = InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.DELEGATE);
	if (openPeriod == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	YearDelegate yearDelegate = null;
	ExecutionSemester executionPeriod = openPeriod.getExecutionPeriod();
	for (Delegate delegate : AccessControl.getPerson().getStudent().getDelegates()) {
	    if (delegate instanceof YearDelegate) {
		if (delegate.isActiveForExecutionYear(executionPeriod.getExecutionYear())) {
		    if (yearDelegate == null
			    || delegate.getDelegateFunction().getEndDate().isAfter(
				    yearDelegate.getDelegateFunction().getEndDate())) {
			yearDelegate = (YearDelegate) delegate;
		    }
		}
	    }
	}

	if (yearDelegate != null) {
	    request.setAttribute("delegate", yearDelegate);
	    request.setAttribute("executionPeriod", executionPeriod);
	    request.setAttribute("answeredExecutionCourses", yearDelegate.getAnsweredInquiriesExecutionCourses(executionPeriod));
	    request.setAttribute("notAnsweredExecutionCourses", yearDelegate
		    .getNotAnsweredInquiriesExecutionCourses(executionPeriod));
	    return actionMapping.findForward("chooseCoursesToAnswer");
	}

	return actionMapping.findForward("inquiriesClosed");
    }

    public ActionForward showFillInquiryPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Delegate delegate = rootDomainObject.readDelegateByOID(getIntegerFromRequest(request, "delegateID"));
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getIntegerFromRequest(request,
		"executionCourseID"));

	request.setAttribute("inquiryDTO", new YearDelegateCourseInquiryDTO(executionCourse, (YearDelegate) delegate));

	return actionMapping.findForward("inquiry1stPage");
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final YearDelegateCourseInquiryDTO inquiryDTO = (YearDelegateCourseInquiryDTO) getRenderedObject("inquiryDTO");
	if (!inquiryDTO.isValid()) {
	    request.setAttribute("inquiryDTO", inquiryDTO);
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("inquiry1stPage");
	}
	YearDelegateCourseInquiry.makeNew(inquiryDTO);
	return showCoursesToAnswerPage(actionMapping, actionForm, request, response);
    }

}
