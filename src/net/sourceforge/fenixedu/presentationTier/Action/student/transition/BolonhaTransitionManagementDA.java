package net.sourceforge.fenixedu.presentationTier.Action.student.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;

public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
	return getStudent(request).getTransitionRegistrations();

    }

    private Student getStudent(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent();
    }

}
