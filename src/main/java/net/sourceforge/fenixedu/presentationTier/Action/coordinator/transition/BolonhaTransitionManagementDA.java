package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/bolonhaTransitionManagement", formBean = "bolonhaTransitionManagementForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "showStudentCurricularPlan", path = "/coordinator/transition/bolonha/showStudentCurricularPlan.jsp"),
        @Forward(name = "chooseRegistration", path = "/coordinator/transition/bolonha/chooseRegistration.jsp"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
        return getStudent(request).getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(
                getLoggedPerson(request));

    }

    private Student getStudent(final HttpServletRequest request) {
        return getDomainObject(request, "studentId");
    }
}
