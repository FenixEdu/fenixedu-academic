package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

@GroupOperator("departmentPresident")
public class DepartmentPresidentStrategy extends GroupStrategy {

    private static final long serialVersionUID = -3153992434314606564L;

    @Override
    public Set<User> getMembers() {
        return FluentIterable.from(Bennu.getInstance().getDepartmentsSet()).transform(new Function<Department, User>() {
            @Override
            public User apply(Department input) {
                return input.getCurrentDepartmentPresident().getUser();
            }
        }).filter(Predicates.notNull()).toSet();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getEmployee() != null
                && user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace() != null
                && user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
                        .isCurrentDepartmentPresident(user.getPerson());
    }

}
