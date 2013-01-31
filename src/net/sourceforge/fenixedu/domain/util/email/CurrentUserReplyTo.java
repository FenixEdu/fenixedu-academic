package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CurrentUserReplyTo extends CurrentUserReplyTo_Base {

	public CurrentUserReplyTo() {
		super();
	}

	@Override
	public String getReplyToAddress(final Person person) {
		final Person currentUser = AccessControl.getPerson();
		final Person toUse = person == null ? currentUser : person;
		final EmailAddress emailAddress = toUse == null ? null : toUse.getDefaultEmailAddress();
		return emailAddress == null ? "" : emailAddress.getValue();
	}

	@Override
	public String getReplyToAddress() {
		final Person currentUser = AccessControl.getPerson();
		final EmailAddress emailAddress = currentUser == null ? null : currentUser.getDefaultEmailAddress();
		return emailAddress == null ? "" : emailAddress.getValue();
	}

}
