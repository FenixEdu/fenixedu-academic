/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries.TeachingInquiryServices;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.person.RoleType;
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
	@Forward(name = "inquiryAnswered", path = "teaching-inquiries.inquiryAnswered"),
	@Forward(name = "inquiryUnavailable", path = "teaching-inquiries.inquiryUnavailable"),
	@Forward(name = "showInquiry1stPage", path = "teaching-inquiries.showInquiry1stPage"),
	@Forward(name = "showInquiry2ndPage", path = "teaching-inquiries.showInquiry2ndPage"),
	@Forward(name = "showInquiry3rdPage", path = "teaching-inquiries.showInquiry3rdPage"),
	@Forward(name = "confirmSubmission", path = "teaching-inquiries.confirmSubmission"),
	@Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp", useTile = false),
	@Forward(name = "showCourseInquiryResult_v2", path = "/inquiries/showCourseInquiryResult_v2.jsp", useTile = false),
	@Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp", useTile = false),
	@Forward(name = "showTeachingInquiryResult_v2", path = "/inquiries/showTeachingInquiryResult_v2.jsp", useTile = false) })
public class TeachingInquiryDA extends FenixDispatchAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
	Professorship professorship = getProfessorship(executionCourse);

	if (executionCourse.getExecutionPeriod().getTeachingInquiryResponsePeriod() == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	if (!executionCourse.getAvailableForInquiries()) {
	    return actionMapping.findForward("inquiryUnavailable");
	}

	request.setAttribute("professorship", professorship);
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

	for (Professorship otherTeacherProfessorship : professorship.isResponsibleFor() ? professorship.getExecutionCourse()
		.getProfessorships() : Collections.singletonList(professorship)) {
	    for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.getInternalDegreeDisclosure()) {
		    courseResultsMap.get(studentInquiriesTeachingResult.getExecutionDegree()).addStudentInquiriesTeachingResult(
			    studentInquiriesTeachingResult);
		}
	    }
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

	request.setAttribute("teachingInquiry", teachingInquiry);
	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	RenderUtils.invalidateViewState();

	if (!teachingInquiry.getFirstPageFirstBlock().validate()
		|| !teachingInquiry.getFirstPageSecondBlockFirstPart().validate()
		|| !teachingInquiry.getFirstPageSecondBlockSecondPart().validate()
		|| !teachingInquiry.getFirstPageSecondBlockFourthPart().validate()
		|| !teachingInquiry.getFirstPageThirdBlock().validate() || !teachingInquiry.getFirstPageFourthBlock().validate()) {

	    addActionMessage(request, "error.inquiries.fillAllFields");
	    return actionMapping.findForward("showInquiry1stPage");
	}

	if (!teachingInquiry.getProfessorship().isResponsibleFor()) {
	    return actionMapping.findForward("confirmSubmission");
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
	    addActionMessage(request, "error.inquiries.fillAllFields");
	    return actionMapping.findForward("showInquiry2ndPage");
	}

	Collection<StudentInquiriesCourseResultBean> inquiriesCourseResults = populateStudentInquiriesCourseResults(teachingInquiry
		.getProfessorship());
	if (checkIfAnyUnsatisfactoryResult(inquiriesCourseResults)) {
	    return forwardTo3rdPage(actionMapping, request, inquiriesCourseResults);
	}

	return actionMapping.findForward("confirmSubmission");
    }

    private boolean checkIfAnyUnsatisfactoryResult(Collection<StudentInquiriesCourseResultBean> inquiriesCourseResults) {
	for (final StudentInquiriesCourseResultBean inquiriesCourseResultBean : inquiriesCourseResults) {
	    if (inquiriesCourseResultBean.getStudentInquiriesCourseResult().isUnsatisfactory()) {
		return true;
	    }

	    for (final StudentInquiriesTeachingResult studentInquiriesTeachingResult : inquiriesCourseResultBean
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.isUnsatisfactory()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public ActionForward submitInquiries3rdPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	if (!teachingInquiry.getThirdPageNinthBlock().validate()) {
	    request.setAttribute("studentInquiriesCourseResults", populateStudentInquiriesCourseResults(teachingInquiry
		    .getProfessorship()));
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillAllFields");
	    return actionMapping.findForward("showInquiry3rdPage");
	}
	return actionMapping.findForward("confirmSubmission");
    }

    public ActionForward backFromPrepareConfirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	if (!teachingInquiry.getProfessorship().isResponsibleFor()) {
	    return showInquiries1stPage(actionMapping, actionForm, request, response);
	} else {
	    Collection<StudentInquiriesCourseResultBean> inquiriesCourseResults = populateStudentInquiriesCourseResults(teachingInquiry
		    .getProfessorship());
	    if (checkIfAnyUnsatisfactoryResult(inquiriesCourseResults)) {
		return forwardTo3rdPage(actionMapping, request, inquiriesCourseResults);
	    } else {
		return showInquiries2ndPage(actionMapping, actionForm, request, response);
	    }
	}
    }

    private ActionForward forwardTo3rdPage(ActionMapping actionMapping, HttpServletRequest request,
	    Collection<StudentInquiriesCourseResultBean> inquiriesCourseResults) {
	final TeachingInquiryDTO teachingInquiry = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	request.setAttribute("teachingInquiry", teachingInquiry);
	request.setAttribute("executionCourse", teachingInquiry.getProfessorship().getExecutionCourse());
	request.setAttribute("studentInquiriesCourseResults", inquiriesCourseResults);
	RenderUtils.invalidateViewState();
	return actionMapping.findForward("showInquiry3rdPage");
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeachingInquiryDTO teachingInquiryDTO = (TeachingInquiryDTO) getRenderedObject("teachingInquiry");
	TeachingInquiryServices.saveAnswers(teachingInquiryDTO);
	request.setAttribute("executionCourse", teachingInquiryDTO.getProfessorship().getExecutionCourse());
	request.setAttribute("executionCoursesToAnswer", AccessControl.getPerson().getTeacher()
		.getExecutionCoursesWithTeachingInquiriesToAnswer());
	return actionMapping.findForward("inquiryAnswered");
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
	return AccessControl.getPerson().getProfessorshipByExecutionCourse(executionCourse);
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentInquiriesCourseResult courseResult = RootDomainObject.getInstance().readStudentInquiriesCourseResultByOID(
		Integer.valueOf(getFromRequest(request, "resultId").toString()));
	final Person loggedPerson = AccessControl.getPerson();
	if (!loggedPerson.isPedagogicalCouncilMember() && loggedPerson.getPersonRole(RoleType.GEP) == null
		&& !loggedPerson.getTeacher().hasProfessorshipForExecutionCourse(courseResult.getExecutionCourse())
		&& courseResult.getExecutionDegree().getCoordinatorByTeacher(loggedPerson) == null) {
	    return null;
	}
	request.setAttribute("inquiryResult", courseResult);
	return actionMapping.findForward(getCourseInquiryResultTemplate(courseResult));
    }

    private static String getCourseInquiryResultTemplate(final StudentInquiriesCourseResult courseResult) {
	final ExecutionSemester executionPeriod = courseResult.getExecutionCourse().getExecutionPeriod();
	if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
	    return "showCourseInquiryResult";
	}
	return "showCourseInquiryResult_v2";
    }

    public ActionForward showInquiryTeachingResult(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final StudentInquiriesTeachingResult teachingResult = RootDomainObject.getInstance()
		.readStudentInquiriesTeachingResultByOID(Integer.valueOf(getFromRequest(request, "resultId").toString()));
	final Person loggedPerson = AccessControl.getPerson();
	if (!loggedPerson.isPedagogicalCouncilMember() && loggedPerson.getPersonRole(RoleType.GEP) == null
		&& teachingResult.getProfessorship().getTeacher() != loggedPerson.getTeacher()
		&& loggedPerson.getTeacher().isResponsibleFor(teachingResult.getProfessorship().getExecutionCourse()) == null
		&& teachingResult.getExecutionDegree().getCoordinatorByTeacher(loggedPerson) == null) {
	    return null;
	}
	request.setAttribute("inquiryResult", teachingResult);
	return actionMapping.findForward(getTeachingInquiryResultTemplate(teachingResult));
    }

    private static String getTeachingInquiryResultTemplate(final StudentInquiriesTeachingResult teachingResult) {
	final ExecutionSemester executionPeriod = teachingResult.getProfessorship().getExecutionCourse().getExecutionPeriod();
	if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
	    return "showTeachingInquiryResult";
	}
	return "showTeachingInquiryResult_v2";
    }

}
