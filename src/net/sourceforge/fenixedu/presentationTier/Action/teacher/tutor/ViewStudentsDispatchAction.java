package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewStudentsByTutor", module = "teacher")
@Forwards(@Forward(name = "viewStudentsByTutor", path = "studentsByTutor"))
public class ViewStudentsDispatchAction extends ViewStudentsByTutorDispatchAction {

    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Person person = getLoggedPerson(request);
	final Teacher teacher = person.getTeacher();

	getTutorships(request, teacher);

	request.setAttribute("tutor", person);
	return mapping.findForward("viewStudentsByTutor");
    }

}
