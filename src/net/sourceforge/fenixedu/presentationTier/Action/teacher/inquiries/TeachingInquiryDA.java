/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.inquiries.teacher.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.inquiries.teacher.TeachingInquiry;
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
@Mapping(path = "/teachingInquiry", module = "teacher")
@Forwards( { @Forward(name = "chooseDegree", path = "teaching-inquiries.chooseDegree"),
	@Forward(name = "inquiriesClosed", path = "teaching-inquiries.inquiriesClosed"),
	@Forward(name = "showInquiry1stPage", path = "teaching-inquiries.showInquiry1stPage"),
	@Forward(name = "showInquiry2ndPage", path = "teaching-inquiries.showInquiry2ndPage") })
public class TeachingInquiryDA extends FenixDispatchAction {

    public ActionForward showDegreesToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);

	InquiryResponsePeriod responsePeriod = executionCourse.getExecutionPeriod().getInquiryResponsePeriod(
		InquiryResponsePeriodType.TEACHING);
	if (responsePeriod == null || !responsePeriod.isOpen()) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	List<ExecutionDegree> degreesWithoutTeachingInquiry = new ArrayList<ExecutionDegree>();

	Professorship professorship = getProfessorship(executionCourse);
	for (final StudentInquiriesTeachingResult studentInquiriesTeachingResult : professorship
		.getStudentInquiriesTeachingResults()) {
	    final ExecutionDegree executionDegree = studentInquiriesTeachingResult.getExecutionDegree();
	    TeachingInquiry teachingInquiry = professorship.getTeachingInquiry(executionDegree);
	    if (teachingInquiry == null) {
		degreesWithoutTeachingInquiry.add(executionDegree);
	    }
	}

	// TEMP HACK
	if (degreesWithoutTeachingInquiry.isEmpty()) {
	    Collection<DegreeCurricularPlan> degreeCurricularPlans = executionCourse.getAssociatedDegreeCurricularPlans();
	    for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
		ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionCourse
			.getExecutionYear());
		if (executionDegree != null) {
		    degreesWithoutTeachingInquiry.add(executionDegree);
		}
	    }
	}

	request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
	request.setAttribute("degreesWithoutTeachingInquiry", degreesWithoutTeachingInquiry);

	return actionMapping.findForward("chooseDegree");
    }

    public ActionForward showInquiries1stPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	if (teachingInquiry == null) {
	    Professorship professorship = getProfessorship(readAndSaveExecutionCourse(request));
	    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(getIntegerFromRequest(request,
		    "executionDegreeID"));
	    teachingInquiry = new TeachingInquiryDTO(professorship, executionDegree);
	}

	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	request.setAttribute("teachingInquiry", teachingInquiry);
	return actionMapping.findForward("showInquiry1stPage");
    }

    public ActionForward showInquiries2ndPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	RenderUtils.invalidateViewState();

	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());

	// if (teachingInquiry.getFirstPageThirdBlock().validate() &&
	// teachingInquiry.getFirstPageFourthBlock().validate()
	// && teachingInquiry.getFirstPageFifthBlock().validate()) {
	return actionMapping.findForward("showInquiry2ndPage");
	// }

	// addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	// return actionMapping.findForward("showInquiry1stPage");
    }

    public ActionForward prepareConfirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	return null;

    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getIntegerFromRequest(request,
		"executionCourseID"));
	request.setAttribute("executionCourse", executionCourse);
	return executionCourse;
    }

    private Professorship getProfessorship(ExecutionCourse executionCourse) {
	return AccessControl.getPerson().getTeacher().getProfessorshipByExecutionCourse(executionCourse);
    }

}
