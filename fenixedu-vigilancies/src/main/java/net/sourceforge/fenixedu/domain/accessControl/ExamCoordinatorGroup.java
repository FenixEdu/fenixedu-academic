package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("examCoordinator")
public class ExamCoordinatorGroup extends GroupStrategy {

    private static final long serialVersionUID = 9028259183901043270L;

    @Override
    public String getPresentationName() {
        return "Exam Coordinator";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getExamCoordinatorsSet().stream()
                .filter(coordinator -> coordinator.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear()))
                .map(coordinator -> coordinator.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getExamCoordinatorsSet().stream()
                        .filter(coordinator -> coordinator.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear()))
                        .findAny().isPresent();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
