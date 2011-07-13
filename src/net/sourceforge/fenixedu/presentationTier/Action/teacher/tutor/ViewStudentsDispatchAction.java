package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean.StudentsPerformanceInfoNullEntryYearBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TutorshipLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/viewStudentsByTutor", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {
	@Forward(name = "viewStudentsByTutor", path = "/teacher/tutor/viewStudentsByTutor.jsp"),
	@Forward(name = "editStudent", path = "/teacher/tutor/editStudent.jsp") })
public class ViewStudentsDispatchAction extends ViewStudentsByTutorDispatchAction {

    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Person person = getLoggedPerson(request);
	final Teacher teacher = person.getTeacher();

	getTutorships(request, teacher);

	request.setAttribute("performanceBean", getOrCreateBean(teacher));
	request.setAttribute("tutor", person);
	return mapping.findForward("viewStudentsByTutor");
    }

    public StudentsPerformanceInfoNullEntryYearBean getOrCreateBean(Teacher teacher) {
	StudentsPerformanceInfoNullEntryYearBean performanceBean = getRenderedObject("performanceBean");
	if (performanceBean == null) {
	    performanceBean = StudentsPerformanceInfoNullEntryYearBean.create(teacher);
	}

	return performanceBean;
    }

    public ActionForward editStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Student student = rootDomainObject.readStudentByOID(Integer.valueOf(request.getParameter("studentID")));

	Registration registration = rootDomainObject.readRegistrationByOID(Integer
		.valueOf(request.getParameter("registrationID")));
	TutorshipLog tutorshipLog = registration.getActiveTutorship().getTutorshipLog();

	request.setAttribute("tutor", getLoggedPerson(request));
	request.setAttribute("student", student);
	request.setAttribute("tutorshipLog", tutorshipLog);
	return mapping.findForward("editStudent");
    }
}
