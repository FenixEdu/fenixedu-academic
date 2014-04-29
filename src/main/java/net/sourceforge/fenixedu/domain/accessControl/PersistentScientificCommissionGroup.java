package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ScientificCommission;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import com.google.common.base.Strings;

@CustomGroupOperator("scientificCommission")
public class PersistentScientificCommissionGroup extends PersistentScientificCommissionGroup_Base {
    protected PersistentScientificCommissionGroup(Degree degree) {
        super();
        setDegree(degree);
    }

    @CustomGroupArgument
    public static Argument<Degree> degreeArgument() {
        return new SimpleArgument<Degree, PersistentScientificCommissionGroup>() {
            @Override
            public Degree parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : Degree.readBySigla(argument);
            }

            @Override
            public Class<? extends Degree> getType() {
                return Degree.class;
            }

            @Override
            public String extract(PersistentScientificCommissionGroup group) {
                return group.getDegree() != null ? group.getDegree().getSigla() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegree().getNameI18N().getContent() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ScientificCommission member : getDegree().getCurrentScientificCommissionMembers()) {
            User user = member.getPerson().getUser();
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
        if (user == null) {
            return false;
        }
        for (ScientificCommission member : user.getPerson().getScientificCommissionsSet()) {
            if (member.getExecutionDegree().getDegree().equals(getDegree())) {
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
        setDegree(null);
        super.gc();
    }

    public static PersistentScientificCommissionGroup getInstance(Degree degree) {
        PersistentScientificCommissionGroup instance = degree.getScientificCommissionGroup();
        return instance != null ? instance : create(degree);
    }

    private static PersistentScientificCommissionGroup create(Degree degree) {
        PersistentScientificCommissionGroup instance = degree.getScientificCommissionGroup();
        return instance != null ? instance : new PersistentScientificCommissionGroup(degree);
    }
}
