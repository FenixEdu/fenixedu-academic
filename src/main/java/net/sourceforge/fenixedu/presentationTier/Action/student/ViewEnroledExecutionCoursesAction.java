package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadEnroledExecutionCourses;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/viewEnroledExecutionCourses", attribute = "enroledExecutionCoursesForm",
        formBean = "enroledExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showEnroledExecutionCourses", path = "/student/viewEnroledExecutionCourses_bd.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.groups")),
        @Forward(name = "showActiveRegistrations", path = "/student/viewActiveRegistrations.jsp") })
public class ViewEnroledExecutionCoursesAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        final Student student = getLoggedPerson(request).getStudent();
        final List<Registration> registrations = student.getActiveRegistrations();

        if (registrations.size() == 1) {
            request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registrations.get(0)));
            return mapping.findForward("showEnroledExecutionCourses");

        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("showActiveRegistrations");
        }
    }

    public ActionForward select(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        final Registration registration =
                getRegistrationByID(getLoggedPerson(request).getStudent(), getIntegerFromRequest(request, "registrationId"));
        request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registration));
        return mapping.findForward("showEnroledExecutionCourses");
    }

    private Registration getRegistrationByID(final Student student, final Integer registrationId) {
        for (final Registration registration : student.getActiveRegistrations()) {
            if (registration.getIdInternal().equals(registrationId)) {
                return registration;
            }
        }
        return null;
    }
}