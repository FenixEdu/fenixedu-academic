/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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
@Mapping(path = "/teachingInquiry", module = "teacher")
@Forwards( { @Forward(name = "inquiryResultsResume", path = "teaching-inquiries.inquiryResultsResume"),
	@Forward(name = "inquiriesClosed", path = "teaching-inquiries.inquiriesClosed"),
	@Forward(name = "inquiryAnswered", path = "teaching-inquiries.inquiryAnswered"),
	@Forward(name = "inquiryUnavailable", path = "teaching-inquiries.inquiryUnavailable"),
	@Forward(name = "teacherInquiry", path = "teaching-inquiries.teacherInquiry"),
	@Forward(name = "delegateInquiry", path = "teaching-inquiries.delegateInquiry") })
public class TeachingInquiryDA extends FenixDispatchAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
	Professorship professorship = getProfessorship(executionCourse);

	if (!executionCourse.getAvailableForInquiries()) {
	    return actionMapping.findForward("inquiryUnavailable");
	}

	TeacherInquiryTemplate inquiryTemplate = TeacherInquiryTemplate.getCurrentTemplate();
	if (inquiryTemplate == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	List<TeacherShiftTypeGroupsResumeResult> teacherResults = new ArrayList<TeacherShiftTypeGroupsResumeResult>();

	List<InquiryResult> professorshipResults = professorship.getInquiryResults();
	if (!professorshipResults.isEmpty()) {
	    InquiryResponseState finalState = InquiryResponseState.COMPLETE;
	    for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		List<InquiryResult> teacherShiftResults = professorship.getInquiryResults(shiftType);
		if (!teacherShiftResults.isEmpty()) {
		    TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult = new TeacherShiftTypeGroupsResumeResult(
			    professorship, shiftType, ResultPersonCategory.TEACHER, "label.inquiry.shiftType", RenderUtils
				    .getEnumString(shiftType));
		    InquiryResponseState completionStateType = teacherShiftTypeGroupsResumeResult.getCompletionStateType();
		    if (finalState == null) {
			finalState = completionStateType;
		    } else {
			finalState = finalState.compareTo(completionStateType) > 0 ? finalState : completionStateType;
		    }
		    teacherResults.add(teacherShiftTypeGroupsResumeResult);
		}
	    }
	    request.setAttribute("completionState", finalState.getLocalizedName());
	}
	Collections.sort(teacherResults, new BeanComparator("shiftType"));

	List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
	for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
	    coursesResultResume.add(new CurricularCourseResumeResult(executionCourse, executionDegree, "label.inquiry.degree",
		    executionDegree.getDegree().getSigla()));
	}
	Collections.sort(coursesResultResume, new BeanComparator("firstPresentationName"));

	request.setAttribute("professorship", professorship);
	request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
	request.setAttribute("teacherResults", teacherResults);
	request.setAttribute("coursesResultResume", coursesResultResume);

	return actionMapping.findForward("inquiryResultsResume");
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());
	TeacherInquiryTemplate teacherInquiryTemplate = TeacherInquiryTemplate.getCurrentTemplate();
	InquiryTeacherAnswer inquiryTeacherAnswer = professorship.getInquiryTeacherAnswer();

	TeacherInquiryBean teacherInquiryBean = new TeacherInquiryBean(teacherInquiryTemplate, professorship,
		inquiryTeacherAnswer);

	request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
	request.setAttribute("executionCourse", professorship.getExecutionCourse());
	request.setAttribute("teacherInquiryBean", teacherInquiryBean);

	return actionMapping.findForward("teacherInquiry");
    }

    public ActionForward showDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID")
		.toString());
	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());

	DelegateInquiryTemplate delegateInquiryTemplate = DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionCourse
		.getExecutionPeriod());
	InquiryDelegateAnswer inquiryDelegateAnswer = null;
	for (InquiryDelegateAnswer delegateAnswer : executionCourse.getInquiryDelegatesAnswers()) {
	    if (delegateAnswer.getExecutionDegree() == executionDegree) {
		inquiryDelegateAnswer = delegateAnswer;
		break;
	    }
	}

	Set<InquiryBlockDTO> delegateInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
	    delegateInquiryBlocks.add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
	}

	Integer year = inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getDelegate().getCurricularYear().getYear() : null;
	request.setAttribute("year", year);
	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionDegree", executionDegree);
	request.setAttribute("delegateInquiryBlocks", delegateInquiryBlocks);
	return actionMapping.findForward("delegateInquiry");
    }

    public ActionForward saveChanges(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final TeacherInquiryBean teacherInquiryBean = getRenderedObject("teacherInquiryBean");
	if (!teacherInquiryBean.isValid()) {
	    request.setAttribute("teacherInquiryBean", teacherInquiryBean);
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("teacherInquiry");
	}
	RenderUtils.invalidateViewState("teacherInquiryBean");
	teacherInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.TEACHER);

	request.setAttribute("executionCourse", teacherInquiryBean.getProfessorship().getExecutionCourse());
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
	return AccessControl.getPerson().getProfessorshipByExecutionCourse(executionCourse);
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentInquiriesCourseResult courseResult = RootDomainObject.getInstance().readStudentInquiriesCourseResultByOID(
		Integer.valueOf(getFromRequest(request, "resultId").toString()));
	final Person loggedPerson = AccessControl.getPerson();
	if (!loggedPerson.isPedagogicalCouncilMember() && loggedPerson.getPersonRole(RoleType.GEP) == null
		&& loggedPerson.getPersonRole(RoleType.DEPARTMENT_MEMBER) == null
		&& !loggedPerson.hasProfessorshipForExecutionCourse(courseResult.getExecutionCourse())
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
		&& loggedPerson.getPersonRole(RoleType.DEPARTMENT_MEMBER) == null
		&& teachingResult.getProfessorship().getPerson() != loggedPerson
		&& loggedPerson.isResponsibleFor(teachingResult.getProfessorship().getExecutionCourse()) == null
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
