package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ScientificCommission;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("scientificCommission")
public class ScientificCommissionGroup extends FenixGroup {
    private static final long serialVersionUID = 8999642876831933207L;

    @GroupArgument
    private Degree degree;

    private ScientificCommissionGroup() {
        super();
    }

    private ScientificCommissionGroup(Degree degree) {
        this();
        this.degree = degree;
    }

    public static ScientificCommissionGroup get(Degree degree) {
        return new ScientificCommissionGroup(degree);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degree.getNameI18N().getContent() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ScientificCommission member : degree.getCurrentScientificCommissionMembers()) {
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
            if (member.getExecutionDegree().getDegree().equals(degree)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentScientificCommissionGroup.getInstance(degree);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ScientificCommissionGroup) {
            return Objects.equal(degree, ((ScientificCommissionGroup) object).degree);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree);
    }
}
