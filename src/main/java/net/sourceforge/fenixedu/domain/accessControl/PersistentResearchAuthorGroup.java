package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;

@CustomGroupOperator("researchAuthor")
public class PersistentResearchAuthorGroup extends PersistentResearchAuthorGroup_Base {
    protected PersistentResearchAuthorGroup() {
        super();
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (User user : Bennu.getInstance().getUserSet()) {
            if (!user.getPerson().getResultParticipationsSet().isEmpty()) {
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
        return user != null && !user.getPerson().getResultParticipationsSet().isEmpty();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentResearchAuthorGroup getInstance() {
        Optional<PersistentResearchAuthorGroup> instance = find(PersistentResearchAuthorGroup.class);
        return instance.isPresent() ? instance.get() : create();
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentResearchAuthorGroup create() {
        Optional<PersistentResearchAuthorGroup> instance = find(PersistentResearchAuthorGroup.class);
        return instance.isPresent() ? instance.get() : new PersistentResearchAuthorGroup();
    }
}
