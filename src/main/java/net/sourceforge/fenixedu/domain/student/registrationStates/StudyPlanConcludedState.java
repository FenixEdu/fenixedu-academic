package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;

import org.joda.time.DateTime;

public class StudyPlanConcludedState extends StudyPlanConcludedState_Base {

    protected StudyPlanConcludedState(Registration registration, Person person, DateTime dateTime) {
        super();
        init(registration, person, dateTime);
        registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
//	if (registration.getStudent().getRegistrationsCount() == 1) {
//	    registration.getPerson().removeRoleByType(RoleType.STUDENT);
//	}
    }

    @Override
    public RegistrationStateType getStateType() {
        return RegistrationStateType.STUDYPLANCONCLUDED;
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
        throw new DomainException("error.impossible.to.forward.from.studyPlanConcluded");
    }

}
