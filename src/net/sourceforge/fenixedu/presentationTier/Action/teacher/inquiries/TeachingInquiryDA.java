/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
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
@Forwards( { @Forward(name = "inquiryPrePage", path = "teaching-inquiries.inquiryPrePage"),
	@Forward(name = "inquiriesClosed", path = "teaching-inquiries.inquiriesClosed"),
	@Forward(name = "showInquiry1stPage", path = "teaching-inquiries.showInquiry1stPage"),
	@Forward(name = "showInquiry2ndPage", path = "teaching-inquiries.showInquiry2ndPage"),
	@Forward(name = "showInquiry3rdPage", path = "teaching-inquiries.showInquiry3rdPage"),
	@Forward(name = "confirmSubmission", path = "teaching-inquiries.confirmSubmission") })
public class TeachingInquiryDA extends FenixDispatchAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
	Professorship professorship = getProfessorship(executionCourse);

	if (!professorship.hasTeachingInquiriesToAnswer()) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	request.setAttribute("studentInquiriesCourseResults", populateStudentInquiriesCourseResults(professorship));
	request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());

	return actionMapping.findForward("inquiryPrePage");
    }

    private Collection<StudentInquiriesCourseResultBean> populateStudentInquiriesCourseResults(final Professorship professorship) {
	Map<ExecutionDegree, StudentInquiriesCourseResultBean> courseResultsMap = new HashMap<ExecutionDegree, StudentInquiriesCourseResultBean>();
	for (StudentInquiriesCourseResult studentInquiriesCourseResult : professorship.getExecutionCourse()
		.getStudentInquiriesCourseResults()) {
	    courseResultsMap.put(studentInquiriesCourseResult.getExecutionDegree(), new StudentInquiriesCourseResultBean(
		    studentInquiriesCourseResult));
	}

	for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : professorship.getStudentInquiriesTeachingResults()) {
	    courseResultsMap.get(studentInquiriesTeachingResult.getExecutionDegree()).addStudentInquiriesTeachingResult(
		    studentInquiriesTeachingResult);
	}

	return courseResultsMap.values();
    }

    public ActionForward showInquiries1stPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	if (teachingInquiry == null) {
	    Professorship professorship = getProfessorship(readAndSaveExecutionCourse(request));

	    if (AccessControl.getPerson().getTeacher() != professorship.getTeacher()) {
		return null;
	    }

	    teachingInquiry = new TeachingInquiryDTO(professorship);
	}

	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	request.setAttribute("teachingInquiry", teachingInquiry);
	return actionMapping.findForward("showInquiry1stPage");
    }

    public ActionForward showInquiries2ndPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");

	if (!teachingInquiry.getProfessorship().isResponsibleFor()) {
	    return prepareConfirm(actionMapping, actionForm, request, response);
	}

	request.setAttribute("teachingInquiry", teachingInquiry);
	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	RenderUtils.invalidateViewState();

	if (!teachingInquiry.getFirstPageFirstBlock().validate()
		|| !teachingInquiry.getFirstPageSecondBlockFirstPart().validate()
		|| !teachingInquiry.getFirstPageSecondBlockSecondPart().validate()
		|| !teachingInquiry.getFirstPageSecondBlockThirdPart().validate()
		|| !teachingInquiry.getFirstPageSecondBlockFourthPart().validate()
		|| !teachingInquiry.getFirstPageThirdBlock().validate()) {

	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("showInquiry1stPage");
	}

	return actionMapping.findForward("showInquiry2ndPage");
    }

    public ActionForward showInquiries3rdPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	final ExecutionCourse executionCourse = teachingInquiry.getProfessorship().getExecutionCourse();
	request.setAttribute("executionCourse", executionCourse);

	if (!teachingInquiry.getSecondPageFourthBlock().validate()
		|| !teachingInquiry.getSecondPageFourthBlockThirdPart().validate()
		|| !teachingInquiry.getSecondPageFifthBlockFirstPart().validate()
		|| !teachingInquiry.getSecondPageFifthBlockSecondPart().validate()
		|| !teachingInquiry.getSecondPageSixthBlock().validate()
		|| !teachingInquiry.getSecondPageSeventhBlock().validate()
		|| !teachingInquiry.getSecondPageEighthBlock().validate()) {

	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("showInquiry2ndPage");
	}

	// populateStudentInquiriesCourseResults()
	for (final StudentInquiriesCourseResult studentInquiriesCourseResult : executionCourse.getStudentInquiriesCourseResults()) {

	}

	if (false) {// check unsatisfactory results
	    return prepareConfirm(actionMapping, actionForm, request, response);
	}

	RenderUtils.invalidateViewState();
	return actionMapping.findForward("showInquiry3rdPage");
    }

    public ActionForward submitInquiries3rdPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	if (!teachingInquiry.getThirdPageNinthBlock().validate()) {
	    request.setAttribute("teachingInquiry", teachingInquiry);
	    request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("showInquiry3rdPage");
	}
	return prepareConfirm(actionMapping, actionForm, request, response);
    }

    public ActionForward prepareConfirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	return actionMapping.findForward("confirmSubmission");
    }

    public ActionForward backFromPrepareConfirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	if (!teachingInquiry.getProfessorship().isResponsibleFor()) {
	    return showInquiries1stPage(actionMapping, actionForm, request, response);
	} else {
	    if (false) {// check unsatisfactory results
		return showInquiries2ndPage(actionMapping, actionForm, request, response);
	    } else {
		return showInquiries3rdPage(actionMapping, actionForm, request, response);
	    }
	}
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiryDTO = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	TeachingInquiry.makeNew(teachingInquiryDTO);
	request.setAttribute("executionCourse", teachingInquiryDTO.getProfessorship().getExecutionCourse());
	return showInquiriesPrePage(actionMapping, actionForm, request, response);
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getIntegerFromRequest(request,
		"executionCourseID"));
	if (executionCourse == null) {
	    return (ExecutionCourse) request.getAttribute("executionCourse");
	}
	request.setAttribute("executionCourse", executionCourse);
	return executionCourse;
    }

    private Professorship getProfessorship(ExecutionCourse executionCourse) {
	return AccessControl.getPerson().getTeacher().getProfessorshipByExecutionCourse(executionCourse);
    }

}
