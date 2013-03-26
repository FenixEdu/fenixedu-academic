package net.sourceforge.fenixedu.domain.util.email;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.ist.fenixWebFramework.services.Service;

public class EmailAddressThread extends Thread {

    final Group group;
    final Set<String> emailAddresses;

    public EmailAddressThread(final Group group, final Set<String> emailAddresses) {
        this.group = group;
        this.emailAddresses = emailAddresses;
    }

    @Override
    @Service
    public void run() {
        for (final Person person : group.getElements()) {
            final String emailAddress = person.getEmail();
            if (emailAddress != null && !emailAddress.isEmpty()) {
                emailAddresses.add(emailAddress);
            }
        }
    }

}
