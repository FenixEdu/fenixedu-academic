package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;

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
