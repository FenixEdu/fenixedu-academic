package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/bolonhaTransitionManagement", attribute = "bolonhaTransitionManagementForm",
        formBean = "bolonhaTransitionManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showStudentCurricularPlan", path = "/coordinator/transition/bolonha/showStudentCurricularPlan.jsp"),
        @Forward(name = "chooseRegistration", path = "/coordinator/transition/bolonha/chooseRegistration.jsp"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
        return getStudent(request).getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(
                getLoggedPerson(request));

    }

    private Student getStudent(final HttpServletRequest request) {
        return rootDomainObject.readStudentByOID(getRequestParameterAsInteger(request, "studentId"));
    }

}
