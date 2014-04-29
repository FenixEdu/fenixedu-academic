package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

@CustomGroupOperator("delegateStudents")
public class PersistentDelegateStudentsGroup extends PersistentDelegateStudentsGroup_Base {
    private PersistentDelegateStudentsGroup(PersonFunction delegateFunction, FunctionType type) {
        super();
        setDelegateFunction(delegateFunction);
        setType(type);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<PersonFunction> delegateFunctionArgument() {
        return new SimpleArgument<PersonFunction, PersistentDelegateStudentsGroup>() {
            @Override
            public PersonFunction parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<PersonFunction> getDomainObject(argument);
            }

            @Override
            public Class<? extends PersonFunction> getType() {
                return PersonFunction.class;
            }

            @Override
            public String extract(PersistentDelegateStudentsGroup group) {
                return group.getDelegateFunction() != null ? group.getDelegateFunction().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<FunctionType> functionTypeArgument() {
        return new SimpleArgument<FunctionType, PersistentDelegateStudentsGroup>() {
            @Override
            public FunctionType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FunctionType.valueOf(argument);
            }

            @Override
            public Class<? extends FunctionType> getType() {
                return FunctionType.class;
            }

            @Override
            public String extract(PersistentDelegateStudentsGroup group) {
                return group.getType() != null ? group.getType().name() : "";
            }
        };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        Person delegate = getDelegateFunction().getPerson();
        if (delegate.getStudent() != null) {
            for (Student student : delegate.getStudent()
                    .getStudentsResponsibleForGivenFunctionType(getType(), getExecutionYear())) {
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
        final PersonFunction personFunction = getDelegateFunction();
        return ExecutionYear.getExecutionYearByDate(personFunction.getBeginDate());
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setDelegateFunction(null);
        super.gc();
    }

    public static PersistentDelegateStudentsGroup getInstance(PersonFunction delegateFunction, FunctionType type) {
        PersistentDelegateStudentsGroup instance = select(delegateFunction, type);
        return instance != null ? instance : create(delegateFunction, type);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentDelegateStudentsGroup create(PersonFunction delegateFunction, FunctionType type) {
        PersistentDelegateStudentsGroup instance = select(delegateFunction, type);
        return instance != null ? instance : new PersistentDelegateStudentsGroup(delegateFunction, type);
    }

    private static PersistentDelegateStudentsGroup select(PersonFunction delegateFunction, final FunctionType type) {
        return FluentIterable.from(delegateFunction.getDelegateStudentsGroupSet())
                .firstMatch(new Predicate<PersistentDelegateStudentsGroup>() {
                    @Override
                    public boolean apply(PersistentDelegateStudentsGroup group) {
                        return Objects.equal(group.getType(), type);
                    }
                }).orNull();
    }
}
