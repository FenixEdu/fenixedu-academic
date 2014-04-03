package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
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
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;

@CustomGroupOperator("delegate")
public class PersistentDelegatesGroup extends PersistentDelegatesGroup_Base {

    public PersistentDelegatesGroup(Degree degree, FunctionType function) {
        super();
        setDegree(degree);
        setFunction(function);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentDelegatesGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentDelegatesGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<FunctionType> degreeTypeArgument() {
        return new SimpleArgument<FunctionType, PersistentDelegatesGroup>() {
            @Override
            public FunctionType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FunctionType.valueOf(argument);
            }

            @Override
            public Class<? extends FunctionType> getType() {
                return FunctionType.class;
            }

            @Override
            public String extract(PersistentDelegatesGroup group) {
                return group.getFunction() != null ? group.getFunction().name() : "";
            }
        };
    }

    @Override
    public String getPresentationName() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(getPresentationNameBundle(), Language.getLocale());
        if (getDegree() != null) {
            return resourceBundle.getString("label." + getClass().getSimpleName()) + " " + resourceBundle.getString("label.of")
                    + " " + getDegree().getSigla();
        } else {
            return resourceBundle.getString("label." + getClass().getSimpleName() + "." + getFunction().getName());
        }
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.DelegateResources";
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        if (getDegree() == null) {
            for (Function function : Function.readAllActiveFunctionsByType(getFunction())) {
                for (PersonFunction personFunction : function.getActivePersonFunctions()) {
                    User user = personFunction.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } else {
            for (Student student : getDegree().getAllActiveDelegates()) {
                User user = student.getPerson().getUser();
                if (user != null) {
                    users.add(user);
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
        if (user == null || user.getPerson().getStudent() == null) {
            return false;
        }
        if (getDegree() != null) {
            return user.getPerson().getStudent().getLastActiveRegistration().getDegree().equals(getDegree())
                    && getDegree().hasAnyActiveDelegateFunctionForStudent(user.getPerson().getStudent());
        } else {
            return user.getPerson().getStudent().hasActiveDelegateFunction(getFunction());
        }
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentDelegatesGroup getInstance(Degree degree, FunctionType function) {
        PersistentDelegatesGroup instance = select(degree, function);
        return instance != null ? instance : create(degree, function);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentDelegatesGroup create(Degree degree, FunctionType function) {
        PersistentDelegatesGroup instance = select(degree, function);
        return instance != null ? instance : new PersistentDelegatesGroup(degree, function);
    }

    private static PersistentDelegatesGroup select(Degree degree, final FunctionType function) {
        if (degree != null) {
            for (PersistentDelegatesGroup candidate : degree.getDelegatesGroupSet()) {
                if (Objects.equal(candidate.getFunction(), function)) {
                    return candidate;
                }
            }
        } else {
            return filter(PersistentDelegatesGroup.class).firstMatch(new Predicate<PersistentDelegatesGroup>() {
                @Override
                public boolean apply(PersistentDelegatesGroup group) {
                    return Objects.equal(group.getFunction(), function);
                }
            }).orNull();
        }
        return null;
    }
}
