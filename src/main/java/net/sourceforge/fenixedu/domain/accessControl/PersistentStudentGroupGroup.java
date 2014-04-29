package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("studentGroup")
public class PersistentStudentGroupGroup extends PersistentStudentGroupGroup_Base {
    protected PersistentStudentGroupGroup(StudentGroup studentGroup) {
        super();
        setStudentGroup(studentGroup);
    }

    @CustomGroupArgument
    public static Argument<StudentGroup> studentGroupArgument() {
        return new SimpleArgument<StudentGroup, PersistentStudentGroupGroup>() {
            private static final long serialVersionUID = -4731635395938942581L;

            @Override
            public StudentGroup parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<StudentGroup> getDomainObject(argument);
            }

            @Override
            public Class<? extends StudentGroup> getType() {
                return StudentGroup.class;
            }

            @Override
            public String extract(PersistentStudentGroupGroup group) {
                return group.getStudentGroup() != null ? group.getStudentGroup().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getStudentGroup().getGrouping().getName() + " - " + getStudentGroup().getGroupNumber() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Attends attends : getStudentGroup().getAttendsSet()) {
            User user = attends.getRegistration().getPerson().getUser();
            if (user != null) {
                users.add(user);
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
        for (final Attends attends : getStudentGroup().getAttendsSet()) {
            if (attends.getRegistration().getStudent().equals(user.getPerson().getStudent())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setStudentGroup(null);
        super.gc();
    }

    public static PersistentStudentGroupGroup getInstance(final StudentGroup studentGroup) {
        PersistentStudentGroupGroup instance = studentGroup.getStudentGroupGroup();
        return instance != null ? instance : create(studentGroup);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentGroupGroup create(final StudentGroup studentGroup) {
        PersistentStudentGroupGroup instance = studentGroup.getStudentGroupGroup();
        return instance != null ? instance : new PersistentStudentGroupGroup(studentGroup);
    }
}
