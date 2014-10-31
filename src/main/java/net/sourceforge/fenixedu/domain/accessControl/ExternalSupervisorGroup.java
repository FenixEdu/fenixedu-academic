package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("externalSupervisor")
public class ExternalSupervisorGroup extends GroupStrategy {

    private static final long serialVersionUID = -6937187659105721953L;

    @Override
    public String getPresentationName() {
        return "External Supervisor";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getRegistrationProtocolsSet().stream()
                .<Person> flatMap(protocol -> protocol.getSupervisorsSet().stream()).map(Person::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && !user.getPerson().getRegistrationProtocolsSet().isEmpty();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
