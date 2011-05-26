/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RegentInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RegentTeacherResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
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

@Mapping(path = "/regentInquiry", module = "teacher")
@Forwards( { @Forward(name = "inquiryResultsResume", path = "regent.inquiryResultsResume"),
	@Forward(name = "inquiriesClosed", path = "regent.inquiriesClosed"),
	@Forward(name = "inquiryUnavailable", path = "regent.inquiryUnavailable"),
	@Forward(name = "regentInquiry", path = "regent.regentInquiry") })
public class RegentInquiryDA extends FenixDispatchAction {

    public ActionForward showInquiriesPrePage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionCourse executionCourse = readAndSaveExecutionCourse(request);
	Professorship professorship = getProfessorship(executionCourse);

	RegentInquiryTemplate inquiryTemplate = RegentInquiryTemplate.getTemplateByExecutionPeriod(executionCourse
		.getExecutionPeriod());
	if (inquiryTemplate == null) {
	    return actionMapping.findForward("inquiriesClosed");
	} else if (!inquiryTemplate.isOpen()) {
	    request.setAttribute("readMode", "readMode");
	}

	if (!professorship.getPerson().hasToAnswerRegentInquiry(professorship)) {
	    return actionMapping.findForward("inquiryUnavailable");
	}

	List<CurricularCourseResumeResult> coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
	for (ExecutionDegree executionDegree : executionCourse.getExecutionDegrees()) {
	    CurricularCourseResumeResult courseResumeResult = new CurricularCourseResumeResult(executionCourse, executionDegree,
		    "label.inquiry.degree", executionDegree.getDegree().getSigla(), professorship.getPerson(),
		    ResultPersonCategory.TEACHER, true, false, false, false, true);
	    if (courseResumeResult.getResultBlocks().size() > 1) {
		coursesResultResume.add(courseResumeResult);
	    }
	}
	Collections.sort(coursesResultResume, new BeanComparator("firstPresentationName"));

	Map<Professorship, RegentTeacherResultsResume> regentTeachersResumeMap = new HashMap<Professorship, RegentTeacherResultsResume>();

	List<Professorship> teachersWithNoResults = new ArrayList<Professorship>();
	for (Professorship teacherProfessorship : executionCourse.getProfessorships()) {
	    List<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = teacherProfessorship.getInquiryResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult = new TeacherShiftTypeGroupsResumeResult(
				teacherProfessorship, shiftType, ResultPersonCategory.TEACHER, "label.inquiry.shiftType",
				RenderUtils.getEnumString(shiftType), teacherProfessorship == professorship);

			RegentTeacherResultsResume regentTeachersResultsResume = regentTeachersResumeMap
				.get(teacherProfessorship);
			if (regentTeachersResultsResume == null) {
			    regentTeachersResultsResume = new RegentTeacherResultsResume(teacherProfessorship);
			    regentTeachersResumeMap.put(teacherProfessorship, regentTeachersResultsResume);
			}
			regentTeachersResultsResume.addTeacherShiftTypeGroupsResumeResult(teacherShiftTypeGroupsResumeResult);
		    }
		}
	    } else {
		teachersWithNoResults.add(teacherProfessorship);
	    }
	}

	InquiryResponseState finalState = InquiryResponseState.COMPLETE;
	if (!professorship.hasInquiryRegentAnswer()) {
	    finalState = InquiryResponseState.EMPTY;
	} else if (professorship.getInquiryRegentAnswer().hasRequiredQuestionsToAnswer(inquiryTemplate)
		|| professorship.getPerson().hasMandatoryCommentsToMakeAsRegentInUC(executionCourse)
		|| professorship.hasMandatoryCommentsToMakeAsResponsible()) {
	    finalState = InquiryResponseState.INCOMPLETE;
	}

	List<RegentTeacherResultsResume> regentTeachersResumeList = new ArrayList<RegentTeacherResultsResume>(
		regentTeachersResumeMap.values());

	request.setAttribute("completionState", finalState.getLocalizedName());
	Collections.sort(regentTeachersResumeList, new BeanComparator("professorship.person.name"));

	request.setAttribute("professorship", professorship);
	request.setAttribute("executionSemester", executionCourse.getExecutionPeriod());
	request.setAttribute("regentTeachersResumeList", regentTeachersResumeList);
	request.setAttribute("teachersWithNoResults", teachersWithNoResults);
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

    public ActionForward showRegentInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());
	RegentInquiryTemplate regentInquiryTemplate = RegentInquiryTemplate.getCurrentTemplate();

	RegentInquiryBean regentInquiryBean = new RegentInquiryBean(regentInquiryTemplate, professorship);

	request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
	request.setAttribute("executionCourse", professorship.getExecutionCourse());
	request.setAttribute("regentInquiryBean", regentInquiryBean);

	return actionMapping.findForward("regentInquiry");
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());

	TeacherInquiryTemplate teacherInquiryTemplate = TeacherInquiryTemplate.getTemplateByExecutionPeriod(professorship
		.getExecutionCourse().getExecutionPeriod());
	InquiryTeacherAnswer inquiryTeacherAnswer = professorship.getInquiryTeacherAnswer();

	Set<InquiryBlockDTO> teacherInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
	    teacherInquiryBlocks.add(new InquiryBlockDTO(inquiryTeacherAnswer, inquiryBlock));
	}

	request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
	request.setAttribute("executionCourse", professorship.getExecutionCourse());
	request.setAttribute("person", professorship.getPerson());
	request.setAttribute("teacherInquiryBlocks", teacherInquiryBlocks);
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
	final RegentInquiryBean regentInquiryBean = getRenderedObject("regentInquiryBean");

	String validationResult = regentInquiryBean.validateInquiry();
	if (!Boolean.valueOf(validationResult)) {
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

	    request.setAttribute("regentInquiryBean", regentInquiryBean);
	    request.setAttribute("executionPeriod", regentInquiryBean.getProfessorship().getExecutionCourse()
		    .getExecutionPeriod());
	    request.setAttribute("executionCourse", regentInquiryBean.getProfessorship().getExecutionCourse());
	    return actionMapping.findForward("regentInquiry");
	}

	RenderUtils.invalidateViewState("regentInquiryBean");
	regentInquiryBean.saveChanges(getUserView(request).getPerson(), ResultPersonCategory.REGENT);

	request.setAttribute("executionCourse", regentInquiryBean.getProfessorship().getExecutionCourse());
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
}
