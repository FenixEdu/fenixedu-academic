package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CurrentUserReplyTo extends CurrentUserReplyTo_Base {

    public CurrentUserReplyTo() {
	super();
    }

    @Override
    public String getReplyToAddress() {
	return AccessControl.getPerson().getDefaultEmailAddress().getValue();
    }

}
