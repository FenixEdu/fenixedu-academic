package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Employee;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("activeEmployees")
public class ActiveEmployees extends GroupStrategy {

    private static final long serialVersionUID = -2985536595609345377L;

    @Override
    public String getPresentationName() {
        return "Active Employees";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getEmployeesSet().stream().filter(Employee::isActive)
                .map(employee -> employee.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && user.getPerson().getEmployee() != null
                && user.getPerson().getEmployee().isActive();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
