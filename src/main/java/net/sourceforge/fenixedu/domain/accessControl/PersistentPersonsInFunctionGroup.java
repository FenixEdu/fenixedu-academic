package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("function")
public class PersistentPersonsInFunctionGroup extends PersistentPersonsInFunctionGroup_Base {
    protected PersistentPersonsInFunctionGroup(Function function) {
        super();
        setFunction(function);
    }

    @CustomGroupArgument
    public static Argument<Function> functionArgument() {
        return new SimpleArgument<Function, PersistentPersonsInFunctionGroup>() {
            private static final long serialVersionUID = -6722867211173812630L;

            @Override
            public Function parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Function> getDomainObject(argument);
            }

            @Override
            public Class<? extends Function> getType() {
                return Function.class;
            }

            @Override
            public String extract(PersistentPersonsInFunctionGroup group) {
                return group.getFunction() != null ? group.getFunction().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getFunction().getName(), getFunction().getUnit().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (PersonFunction function : getFunction().getPersonFunctions()) {
            if (function.isActive()) {
                User user = function.getPerson().getUser();
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
        return getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentPersonsInFunctionGroup getInstance(Function function) {
        PersistentPersonsInFunctionGroup instance = function.getPersonsInFunctionGroup();
        return instance != null ? instance : create(function);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentPersonsInFunctionGroup create(Function function) {
        PersistentPersonsInFunctionGroup instance = function.getPersonsInFunctionGroup();
        return instance != null ? instance : new PersistentPersonsInFunctionGroup(function);
    }

}
