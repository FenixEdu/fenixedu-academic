package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.CurriculumDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewCurriculum", module = "externalSupervision", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/externalSupervision/consult/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/externalSupervision/consult/showStudentCurriculum.jsp") })
public class ShowStudentCurriculum extends CurriculumDispatchAction {

    public ActionForward prepareForSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final String personId = request.getParameter("personId");
        final Person personStudent = FenixFramework.getDomainObject(personId);
        final Student student = personStudent.getStudent();

        request.setAttribute("student", student);
        return mapping.findForward("chooseRegistration");
    }

    public ActionForward showCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        Registration registration = null;

        final String registrationId = request.getParameter("registrationId");
        registration = FenixFramework.getDomainObject(registrationId);

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCPForSupervisor(registration, mapping, (DynaActionForm) form, request);
        }
    }
}
