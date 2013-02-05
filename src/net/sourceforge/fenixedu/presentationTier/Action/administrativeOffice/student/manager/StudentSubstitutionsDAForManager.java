package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
}