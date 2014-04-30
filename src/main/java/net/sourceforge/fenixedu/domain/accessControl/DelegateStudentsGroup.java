package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("delegateStudents")
public class DelegateStudentsGroup extends FenixGroup {
    private static final long serialVersionUID = -922683188265327080L;

    @GroupArgument
    private PersonFunction delegateFunction;

    @GroupArgument
    private FunctionType type;

    private DelegateStudentsGroup() {
        super();
    }

    private DelegateStudentsGroup(PersonFunction delegateFunction, FunctionType type) {
        this();
        this.delegateFunction = delegateFunction;
        this.type = type;
    }

    public static DelegateStudentsGroup get(PersonFunction delegateFunction, FunctionType type) {
        return new DelegateStudentsGroup(delegateFunction, type);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        Person delegate = delegateFunction.getPerson();
        if (delegate.getStudent() != null) {
            for (Student student : delegate.getStudent().getStudentsResponsibleForGivenFunctionType(type, getExecutionYear())) {
                User user = student.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        } else {
            for (Coordinator coordinator : delegate.getCoordinatorsSet()) {
                final Degree degree = coordinator.getExecutionDegree().getDegree();
                for (Student student : degree.getAllStudents()) {
                    User user = student.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }

        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public ExecutionYear getExecutionYear() {
        return ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentDelegateStudentsGroup.getInstance(delegateFunction, type);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelegateStudentsGroup) {
            DelegateStudentsGroup other = (DelegateStudentsGroup) object;
            return Objects.equal(delegateFunction, other.delegateFunction) && Objects.equal(type, other.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(delegateFunction, type);
    }

}
