package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("allCoordinators")
public class AllCoordinatorsGroup extends GroupStrategy {

    private static final long serialVersionUID = 8879256369998150156L;

    @Override
    public String getPresentationName() {
        return "All Coordinators";
    }

    @Override
    public Set<User> getMembers() {
        return Stream.concat(
                Bennu.getInstance().getCoordinatorsSet().stream().map(coordinator -> coordinator.getPerson().getUser()),
                Bennu.getInstance().getScientificCommissionsSet().stream().map(commission -> commission.getPerson().getUser()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && (!user.getPerson().getCoordinatorsSet().isEmpty() || !user.getPerson().getScientificCommissionsSet().isEmpty());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
