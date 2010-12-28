/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentTeacherInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryNotAnsweredJustification;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@Mapping(path = "/studentInquiry", module = "student", formBean = "inquiryNotAnsweredForm")
@Forwards( { @Forward(name = "chooseCourse", path = "/student/inquiries/chooseCourse.jsp"),
	@Forward(name = "inquiriesClosed", path = "/student/inquiries/inquiriesClosed.jsp"),
	@Forward(name = "showInquiry", path = "/student/inquiries/fillInInquiry.jsp"),
	@Forward(name = "showTeachersToAnswer", path = "/student/inquiries/showTeachersToAnswer.jsp"),
	@Forward(name = "showInquiry1stPage", path = "/student/inquiries/inquiry1stPage.jsp"),
	@Forward(name = "showInquiry2ndPage", path = "/student/inquiries/inquiry2ndPage.jsp"),
	@Forward(name = "chooseTeacher", path = "/student/inquiries/chooseTeacher.jsp"),
	@Forward(name = "showTeacherInquiry1stPage", path = "/student/inquiries/teacherInquiry1stPage.jsp"),
	@Forward(name = "showTeacherInquiry", path = "/student/inquiries/teacherInquiry.jsp"),
	@Forward(name = "showDontRespond", path = "/student/inquiries/dontRespond.jsp"),
	@Forward(name = "previewAndConfirm", path = "/student/inquiries/previewAndConfirm.jsp") })
public class StudentInquiryDA extends FenixDispatchAction {

    public ActionForward showCoursesToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final CurricularCourseInquiryTemplate studentInquiryTemplate = CurricularCourseInquiryTemplate.getCurrentTemplate();
	if (studentInquiryTemplate == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	ExecutionSemester executionSemester = studentInquiryTemplate.getExecutionPeriod(); // ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();

	final Student student = AccessControl.getPerson().getStudent();
	final Collection<StudentInquiryRegistry> coursesToAnswer = student
		.retrieveAndCreateMissingInquiryRegistriesForPeriod(executionSemester);

	boolean isAnyInquiryToAnswer = false;
	final List<CurricularCourseInquiriesRegistryDTO> courses = new ArrayList<CurricularCourseInquiriesRegistryDTO>();
	for (final StudentInquiryRegistry registry : coursesToAnswer) {
	    courses.add(new CurricularCourseInquiriesRegistryDTO(registry));
	    if (registry.isToAnswerLater()) {
		isAnyInquiryToAnswer = true;
	    }
	}

	if (courses.isEmpty() || (!isAnyInquiryToAnswer && student.isWeeklySpentHoursSubmittedForOpenInquiry())) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	request.setAttribute("executionSemester", executionSemester);
	request.setAttribute("courses", courses);
	request.setAttribute("student", student);
	final VariantBean weeklySpentHours = new VariantBean();
	weeklySpentHours.setInteger(null);
	request.setAttribute("weeklySpentHours", weeklySpentHours);
	return actionMapping.findForward("chooseCourse");
    }

    public ActionForward submitWeeklySpentHours(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = StudentInquiryTemplate.getCurrentTemplate().getExecutionPeriod();
	/*
	 * InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.STUDENT
	 * ) .getExecutionPeriod();
	 */

	List<CurricularCourseInquiriesRegistryDTO> courses = getRenderedObject("hoursAndDaysByCourse");
	VariantBean weeklySpentHours = getRenderedObject("weeklySpentHours");

	// try {
	// SubmitStudentSpentTimeInPeriod.run(AccessControl.getPerson().getStudent(),
	// courses, weeklySpentHours.getInteger(),
	// executionSemester);
	// } catch (DomainException e) {
	// addActionMessage(request, e.getKey());
	// }

	Student student = AccessControl.getPerson().getStudent();
	// try {
	student.setSpentTimeInPeriodForInquiry(courses, weeklySpentHours.getInteger(), executionSemester);
	// } catch (DomainException e) {
	// addActionMessage(request, e.getKey());
	// }

	return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

    public ActionForward simulateAWH(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = StudentInquiryTemplate.getCurrentTemplate().getExecutionPeriod();
	// ExecutionSemester executionSemester =
	// InquiryResponsePeriod.readOpenPeriod(InquiryResponsePeriodType.STUDENT)
	// .getExecutionPeriod();

	List<CurricularCourseInquiriesRegistryDTO> courses = getRenderedObject("hoursAndDaysByCourse");
	VariantBean weeklySpentHours = getRenderedObject("weeklySpentHours");

	if (!StudentInquiryRegistry.checkTotalPercentageDistribution(courses)) {
	    addActionMessage(request, "error.weeklyHoursSpentPercentage.is.not.100.percent");
	} else if (!StudentInquiryRegistry.checkTotalStudyDaysSpentInExamsSeason(courses)) {
	    addActionMessage(request, "error.studyDaysSpentInExamsSeason.exceedsMaxDaysLimit");
	} else {
	    RenderUtils.invalidateViewState("hoursAndDaysByCourse");
	    for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
		curricularCourseInquiriesRegistryDTO.setAutonomousWorkHoursForSimulation(weeklySpentHours.getInteger());
	    }
	}

	request.setAttribute("executionSemester", executionSemester);
	request.setAttribute("courses", courses);
	request.setAttribute("student", AccessControl.getPerson().getStudent());
	final VariantBean wsh = new VariantBean();
	wsh.setInteger(null);
	request.setAttribute("weeklySpentHours", wsh);
	return actionMapping.findForward("chooseCourse");
    }

    public ActionForward showJustifyNotAnswered(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm form = (DynaActionForm) actionForm;
	// final Integer inquiryRegistryID = getIntegerFromRequest(request,
	// "inquiryRegistryID");
	String inquiryRegistryID = (String) getFromRequest(request, "inquiryRegistryID");
	form.set("inquiryRegistryID", inquiryRegistryID);

	request.setAttribute("inquiryRegistry", AbstractDomainObject.fromExternalId(inquiryRegistryID));

	return actionMapping.findForward("showDontRespond");
    }

    public ActionForward justifyNotAnswered(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm form = (DynaActionForm) actionForm;
	StudentInquiryRegistry inquiryRegistry = AbstractDomainObject.fromExternalId(form.getString("inquiryRegistryID"));
	String notAnsweredJustification = (String) form.get("notAnsweredJustification");
	if (StringUtils.isEmpty(notAnsweredJustification)) {
	    addActionMessage(request, "error.inquiries.notAnsweredFillAtLeastOneField");
	    return handleDontRespondError(actionMapping, request, inquiryRegistry);
	}

	if (inquiryRegistry.getStudent().getPerson() != AccessControl.getPerson()) {
	    addActionMessage(request, "error.inquiries.notTheSamePerson");
	    return handleDontRespondError(actionMapping, request, inquiryRegistry);
	}

	InquiryNotAnsweredJustification justification = InquiryNotAnsweredJustification.valueOf(notAnsweredJustification);
	String notAnsweredOtherJustification = form.getString("notAnsweredOtherJustification");

	if (justification == InquiryNotAnsweredJustification.OTHER) {
	    if (StringUtils.isEmpty(notAnsweredOtherJustification)) {
		addActionMessage(request, "error.inquiries.fillOtherJustification");
		return handleDontRespondError(actionMapping, request, inquiryRegistry);
	    } else if (notAnsweredOtherJustification.length() > 200) {
		addActionMessage(request, "error.inquiries.fillOtherJustificationLengthOversized");
		return handleDontRespondError(actionMapping, request, inquiryRegistry);
	    }
	}

	InquiryCourseAnswer.createNotAnsweredInquiryCourse(inquiryRegistry, justification, notAnsweredOtherJustification);
	return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

    private ActionForward handleDontRespondError(ActionMapping actionMapping, HttpServletRequest request,
	    StudentInquiryRegistry inquiryRegistry) {
	request.setAttribute("inquiryRegistryID", inquiryRegistry.getExternalId());
	request.setAttribute("inquiryRegistry", inquiryRegistry);
	return actionMapping.findForward("showDontRespond");
    }

    public ActionForward showCurricularInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final StudentInquiryTemplate studentInquiryTemplate = CurricularCourseInquiryTemplate.getCurrentTemplate();
	if (studentInquiryTemplate == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}

	StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");

	if (inquiryBean == null) {
	    Set<InquiryBlockDTO> inquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	    for (InquiryBlock inquiryBlock : studentInquiryTemplate.getInquiryBlocks()) {
		inquiryBlocks.add(new InquiryBlockDTO(inquiryBlock));
	    }
	    String inquiryRegistryID = (String) getFromRequest(request, "inquiryRegistryID");
	    StudentInquiryRegistry inquiryRegistry = AbstractDomainObject.fromExternalId(inquiryRegistryID);

	    inquiryBean = new StudentInquiryBean(StudentTeacherInquiryTemplate.getCurrentTemplate(), inquiryRegistry);
	    inquiryBean.setCurricularCourseBlocks(inquiryBlocks);

	}
	//	StudentInquiryDTO studentInquiry = getRenderedObject("studentInquiry");
	//	if (studentInquiry == null) {
	//	    final InquiriesRegistry inquiriesRegistry = rootDomainObject.readInquiriesRegistryByOID(getIntegerFromRequest(
	//		    request, "inquiriesRegistryID"));
	//	    if (inquiriesRegistry.getStudent().getPerson() != AccessControl.getPerson()) {
	//		// FIXME: ERROR MESSAGE
	//		return null;
	//	    }
	//
	//	    studentInquiry = StudentInquiryDTO.makeNew(inquiriesRegistry);
	//	}

	request.setAttribute("inquiryBean", inquiryBean);
	return actionMapping.findForward("showInquiry");
    }

    public ActionForward showTeachersToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("inquiryBean", inquiryBean);
	if (!inquiryBean.validateCurricularInquiry()) {
	    addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	    return actionMapping.findForward("showInquiry");
	}

	//RenderUtils.invalidateViewState();

	//	if (studentInquiry.getFirstPageThirdBlock().validate() && studentInquiry.getFirstPageFourthBlock().validate()
	//		&& studentInquiry.getFirstPageFifthBlock().validate()) {
	//	    return actionMapping.findForward("showInquiry2ndPage");
	//	}

	//addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	return actionMapping.findForward("showTeachersToAnswer");
    }

    public ActionForward showInquiries2ndPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentInquiryDTO studentInquiry = getRenderedObject("studentInquiry");
	request.setAttribute("studentInquiry", studentInquiry);
	RenderUtils.invalidateViewState();

	if (studentInquiry.getFirstPageThirdBlock().validate() && studentInquiry.getFirstPageFourthBlock().validate()
		&& studentInquiry.getFirstPageFifthBlock().validate()) {
	    return actionMapping.findForward("showInquiry2ndPage");
	}

	addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	return actionMapping.findForward("showInquiry1stPage");
    }

    //    public ActionForward showTeachersToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
    //	    HttpServletResponse response) throws Exception {
    //	final StudentInquiryDTO studentInquiry = getRenderedObject("studentInquiry");
    //	request.setAttribute("studentInquiry", studentInquiry);
    //	RenderUtils.invalidateViewState();
    //
    //	if (studentInquiry.getSecondPageFirstBlock().validate() && studentInquiry.getSecondPageSecondBlock().validate()
    //		&& studentInquiry.getSecondPageThirdBlock().validate()) {
    //	    return actionMapping.findForward("chooseTeacher");
    //	}
    //
    //	addActionMessage(request, "error.inquiries.fillAllRequiredFields");
    //	return actionMapping.findForward("showInquiry2ndPage");
    //    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("teacherInquiry", getRenderedObject("teacherInquiry"));
	request.setAttribute("inquiryBean", getRenderedObject("inquiryBean"));
	return actionMapping.findForward("showTeacherInquiry");
    }

    public ActionForward showTeachersInquiries1stPage(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("teacherInquiry", getRenderedObject("teacherInquiry"));
	request.setAttribute("studentInquiry", getRenderedObject("studentInquiry"));
	return actionMapping.findForward("showTeacherInquiry1stPage");
    }

    public ActionForward fillTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentTeacherInquiryBean teacherInquiry = getRenderedObject("teacherInquiry");
	request.setAttribute("inquiryBean", getRenderedObject("inquiryBean"));
	RenderUtils.invalidateViewState();

	if (!teacherInquiry.validateTeacherInquiry()) {
	    addActionMessage(request, "error.inquiries.fillInQuestion", "5.1.1");
	    request.setAttribute("teacherInquiry", teacherInquiry);
	    return actionMapping.findForward("showTeacherInquiry");
	}
	teacherInquiry.setFilled(true);
	return actionMapping.findForward("showTeachersToAnswer");
	//}

	//	request.setAttribute("teacherInquiry", teacherInquiry);
	//	addActionMessage(request, "error.inquiries.fillAllRequiredFields");
	//	return actionMapping.findForward("showTeacherInquiry1stPage");
    }

    public ActionForward showPreview(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return confirm(actionMapping, actionForm, request, response);

	// request.setAttribute("studentInquiry",
	// getRenderedObject("studentInquiry"));
	// return actionMapping.findForward("previewAndConfirm");
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final StudentInquiryBean inquiryBean = getRenderedObject("inquiryBean");
	//TODO escrever as respostas na BD!!!

	inquiryBean.setAnsweredInquiry();

	return showCoursesToAnswer(actionMapping, actionForm, request, response);
	//	final StudentInquiryDTO studentInquiry = getRenderedObject("studentInquiry");
	//	if (studentInquiry.getInquiriesRegistry().getState() == InquiriesRegistryState.ANSWER_LATER) {
	//	    WriteStudentInquiry.run(studentInquiry);
	//	}
	//	return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

}