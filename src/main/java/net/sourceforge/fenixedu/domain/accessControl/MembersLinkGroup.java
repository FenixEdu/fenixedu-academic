package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("persistentMembers")
public class MembersLinkGroup extends FenixGroup {
    private static final long serialVersionUID = 179427579195576958L;

    @GroupArgument
    private PersistentGroupMembers persistentGroupMembers;

    private MembersLinkGroup() {
        super();
    }

    protected MembersLinkGroup(PersistentGroupMembers persistentGroupMembers) {
        this();
        this.persistentGroupMembers = persistentGroupMembers;
    }

    public static MembersLinkGroup get(PersistentGroupMembers persistentGroupMembers) {
        return new MembersLinkGroup(persistentGroupMembers);
    }

    @Override
    public String getPresentationName() {
        return persistentGroupMembers.getName();
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : persistentGroupMembers.getPersonsSet()) {
            if (person.getUser() != null) {
                users.add(person.getUser());
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
        return user != null && persistentGroupMembers.getPersonsSet().contains(user.getPerson());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentMembersLinkGroup.getInstance(persistentGroupMembers);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MembersLinkGroup) {
            return Objects.equal(persistentGroupMembers, ((MembersLinkGroup) object).persistentGroupMembers);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(persistentGroupMembers);
    }
}
