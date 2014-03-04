package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicPathApp;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.CurriculumDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "view-curriculum", titleKey = "link.student.curriculum")
@Mapping(path = "/viewStudentCurriculum", module = "alumni", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/curriculum/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "alumni.view.curriculum.not.authorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "alumni.view.curriculum.validate.identity", path = "/alumni/notAuthorizedValidateIdentity.jsp") })
public class AlumniViewCurriculum extends CurriculumDispatchAction {

    @EntryPoint
    public ActionForward checkValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (getLoggedPerson(request).getStudent().getAlumni() != null) {

            if (getLoggedPerson(request).getStudent().getAlumni().hasAnyPendingIdentityRequests()) {
                return mapping.findForward("alumni.view.curriculum.validate.identity");
            }
            return super.prepare(mapping, form, request, response);
        } else {
            if (getLoggedPerson(request).hasRole(RoleType.ALUMNI)) {
                return super.prepare(mapping, form, request, response);
            } else {
                return mapping.findForward("alumni.view.curriculum.not.authorized");
            }
        }
    }
}
