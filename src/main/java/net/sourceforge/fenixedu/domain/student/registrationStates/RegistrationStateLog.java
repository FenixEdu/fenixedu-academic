package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.joda.time.DateTime;

public class RegistrationStateLog extends RegistrationStateLog_Base {

    private RegistrationStateLog() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWhenCreated(new DateTime());
    }

    public RegistrationStateLog(final RegistrationState state, final EnrolmentAction action, final Person person) {

        this();
        String[] args = {};

        if (state == null) {
            throw new DomainException("error.RegistrationStateLog.invalid.state", args);
        }
        Object obj = state.getRegistration();
        String[] args1 = {};
        if (obj == null) {
            throw new DomainException("error.RegistrationStateLog.invalid.registation", args1);
        }
        Object obj1 = state.getStateDate();
        String[] args2 = {};
        if (obj1 == null) {
            throw new DomainException("error.RegistrationStateLog.invalid.state.date", args2);
        }
        String[] args3 = {};
        if (action == null) {
            throw new DomainException("error.RegistrationStateLog.invalid.action", args3);
        }

        setRegistration(state.getRegistration());
        setStateDate(state.getStateDate());
        setStateType(state.getClass().getName());
        setAction(action);

        if (person != null) {
            setWho(person.getIstUsername());
        }
    }

    public void delete() {
        removeRootDomainObject();
        removeRegistration();
        super.deleteDomainObject();
    }
}
