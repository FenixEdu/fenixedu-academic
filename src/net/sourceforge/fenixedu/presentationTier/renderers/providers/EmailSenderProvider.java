package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.security.UserView;

public class EmailSenderProvider implements DataProvider {

    public Object provide(final Object source, final Object currentValue) {
	final IUserView userView = UserView.getUser();

	final Set<Sender> senders = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
	for (final Sender sender : RootDomainObject.getInstance().getUtilEmailSendersSet()) {
	    if (sender.getMembers().allows(userView)) {
		senders.add(sender);
	    }
	}

	return senders;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
