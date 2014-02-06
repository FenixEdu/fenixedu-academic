package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.StudentsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentsApp.class, descriptionKey = "label.student.substitutions",
        path = "student-substitutions", titleKey = "label.student.substitutions")
@Mapping(module = "manager", path = "/studentSubstitutions", attribute = "studentDismissalForm",
        formBean = "studentDismissalForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "visualizeRegistration", path = "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans"),
        @Forward(name = "chooseNotNeedToEnrol", path = "/manager/bolonha/dismissal/chooseSubstitutionNotNeedToEnrol.jsp"),
        @Forward(name = "chooseEquivalents", path = "/manager/bolonha/dismissal/chooseSubstitutionEquivalents.jsp"),
        @Forward(name = "chooseExternalCurricularCourse", path = "studentDismissal.chooseExternalCurricularCourse"),
        @Forward(name = "confirmCreateDismissals", path = "/manager/bolonha/dismissal/confirmCreateSubstitution.jsp"),
        @Forward(name = "manage", path = "/manager/bolonha/dismissal/managementDismissals.jsp"),
        @Forward(name = "prepareCreateExternalEnrolment", path = "/manager/bolonha/dismissal/createExternalEnrolment.jsp"),
        @Forward(name = "chooseDismissalEnrolments", path = "/manager/bolonha/dismissal/chooseSubstitutionEnrolments.jsp") })
public class StudentSubstitutionsDAForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentSubstitutionsDA {

    @Override
    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return super.prepare(mapping, form, request, response);
    }
}