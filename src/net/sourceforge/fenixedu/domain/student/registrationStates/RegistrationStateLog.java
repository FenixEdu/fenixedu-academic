package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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

		check(state, "error.RegistrationStateLog.invalid.state");
		check(state.getRegistration(), "error.RegistrationStateLog.invalid.registation");
		check(state.getStateDate(), "error.RegistrationStateLog.invalid.state.date");
		check(action, "error.RegistrationStateLog.invalid.action");

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
