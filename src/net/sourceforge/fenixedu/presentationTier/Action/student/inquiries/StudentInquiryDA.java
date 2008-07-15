/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiryDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/studentInquiry", module = "student")
@Forwards( { @Forward(name = "chooseCourse", path = "/student/inquiries/chooseCourse.jsp"),
	@Forward(name = "inquiriesClosed", path = "/student/inquiries/inquiriesClosed.jsp"),
	@Forward(name = "showInquiry1stPage", path = "/student/inquiries/inquiry1stPage.jsp"),
	@Forward(name = "showInquiry2ndPage", path = "/student/inquiries/inquiry2ndPage.jsp"),
	@Forward(name = "chooseTeacher", path = "/student/inquiries/chooseTeacher.jsp"),
	@Forward(name = "showTeacherInquiry1stPage", path = "/student/inquiries/teacherInquiry1stPage.jsp"),
	@Forward(name = "previewAndConfirm", path = "/student/inquiries/previewAndConfirm.jsp") })
public class StudentInquiryDA extends FenixDispatchAction {

    public ActionForward showCoursesToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final InquiryResponsePeriod lastPeriod = InquiryResponsePeriod.readOpenPeriod();
	if (lastPeriod == null) {
	    return actionMapping.findForward("inquiriesClosed");
	}
	ExecutionSemester executionSemester = lastPeriod.getExecutionPeriod();

	final Student student = AccessControl.getPerson().getStudent();
	final Map<CurricularCourse, InquiriesRegistry> coursesToAnswer = (Map<CurricularCourse, InquiriesRegistry>) executeService(
		"RetrieveOrCreateStudentInquiriesRegistries", student, executionSemester);

	final List<CurricularCourseInquiriesRegistryDTO> courses = new ArrayList<CurricularCourseInquiriesRegistryDTO>();
	for (final Entry<CurricularCourse, InquiriesRegistry> entry : coursesToAnswer.entrySet()) {
	    courses.add(new CurricularCourseInquiriesRegistryDTO(entry.getKey(), entry.getValue()));
	}
	
	if(courses.isEmpty()){
	    return actionMapping.findForward("inquiriesClosed");
	}

	request.setAttribute("courses", courses);
	request.setAttribute("student", student);
	return actionMapping.findForward("chooseCourse");
    }

    public ActionForward submitWeeklySpentHours(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = InquiryResponsePeriod.readOpenPeriod().getExecutionPeriod();

	List<CurricularCourseInquiriesRegistryDTO> courses = (List<CurricularCourseInquiriesRegistryDTO>) getRenderedObject("hoursAndDaysByCourse");
	VariantBean weeklySpentHours = (VariantBean) getRenderedObject("weeklySpentHours");

	try {
	    executeService("SubmitStudentSpentTimeInPeriod", AccessControl.getPerson().getStudent(), courses, weeklySpentHours
		    .getInteger(), executionSemester);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey());
	}

	return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

    public ActionForward showInquiries1stPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	StudentInquiryDTO studentInquiry = (StudentInquiryDTO) getRenderedObject("studentInquiry");
	if (studentInquiry == null) {
	    final InquiriesRegistry inquiriesRegistry = rootDomainObject.readInquiriesRegistryByOID(getIntegerFromRequest(
		    request, "inquiriesRegistryID"));
	    if (inquiriesRegistry.getStudent().getPerson() != AccessControl.getPerson()) {
		// FIXME: ERROR MESSAGE
		return null;
	    }

	    studentInquiry = StudentInquiryDTO.makeNew(inquiriesRegistry);
	}

	request.setAttribute("studentInquiry", studentInquiry);
	return actionMapping.findForward("showInquiry1stPage");
    }

    public ActionForward showInquiries2ndPage(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("studentInquiry", getRenderedObject("studentInquiry"));
	return actionMapping.findForward("showInquiry2ndPage");
    }

    public ActionForward showTeachersToAnswer(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("studentInquiry", getRenderedObject("studentInquiry"));
	return actionMapping.findForward("chooseTeacher");
    }

    public ActionForward showTeachersInquiries1stPage(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final TeacherInquiryDTO teacherInquiry = (TeacherInquiryDTO) getRenderedObject("teacherInquiry");
	teacherInquiry.setFilled(true);
	request.setAttribute("teacherInquiry", teacherInquiry);
	request.setAttribute("studentInquiry", getRenderedObject("studentInquiry"));
	return actionMapping.findForward("showTeacherInquiry1stPage");
    }

    public ActionForward showPreview(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	return confirm(actionMapping, actionForm, request, response);
	
//	request.setAttribute("studentInquiry", getRenderedObject("studentInquiry"));
//	return actionMapping.findForward("previewAndConfirm");
    }

    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final StudentInquiryDTO studentInquiry = (StudentInquiryDTO) getRenderedObject("studentInquiry");
	executeService("WriteStudentInquiry", studentInquiry);
	return showCoursesToAnswer(actionMapping, actionForm, request, response);
    }

}
