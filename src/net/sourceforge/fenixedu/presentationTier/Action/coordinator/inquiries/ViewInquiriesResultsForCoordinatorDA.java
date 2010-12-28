/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/viewInquiriesResults", module = "coordinator", formBeanClass = ViewInquiriesResultPageDTO.class)
@Forwards( { @Forward(name = "inquiryResults", path = "/coordinator/inquiries/viewInquiriesResults.jsp"),
	@Forward(name = "curricularUnitSelection", path = "/coordinator/inquiries/curricularUnitSelection.jsp"),
	@Forward(name = "showFilledTeachingInquiry", path = "/inquiries/showFilledTeachingInquiry.jsp", useTile = false),
	@Forward(name = "showFilledTeachingInquiry_v2", path = "/inquiries/showFilledTeachingInquiry_v2.jsp", useTile = false),
	@Forward(name = "showFilledDelegateInquiry", path = "/inquiries/showFilledDelegateInquiry.jsp", useTile = false),
	@Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp", useTile = false),
	@Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp", useTile = false) })
public class ViewInquiriesResultsForCoordinatorDA extends ViewInquiriesResultsDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    protected boolean coordinatorCanComment(final ExecutionDegree executionDegree, final ExecutionSemester executionPeriod) {
	if (executionDegree.getDegreeType().isThirdCycle()) {
	    return false;
	}

	final InquiryResponsePeriod coordinatorReportResponsePeriod = executionPeriod.getCoordinatorReportResponsePeriod();
	final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
	return coordinator != null && coordinator.isResponsible() && coordinatorReportResponsePeriod != null
		&& coordinatorReportResponsePeriod.isOpen();
    }

}
