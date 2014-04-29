package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("siteManagers")
public class PersistentManagersOfUnitSiteGroup extends PersistentManagersOfUnitSiteGroup_Base {
    protected PersistentManagersOfUnitSiteGroup(UnitSite site) {
        super();
        setUnitSite(site);
    }

    @CustomGroupArgument
    public static Argument<UnitSite> unitSiteArgument() {
        return new SimpleArgument<UnitSite, PersistentManagersOfUnitSiteGroup>() {
            private static final long serialVersionUID = -6722867211173812630L;

            @Override
            public UnitSite parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<UnitSite> getDomainObject(argument);
            }

            @Override
            public Class<? extends UnitSite> getType() {
                return UnitSite.class;
            }

            @Override
            public String extract(PersistentManagersOfUnitSiteGroup group) {
                return group.getUnitSite() != null ? group.getUnitSite().getExternalId() : null;
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getUnitSite().getUnit().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : getUnitSite().getManagersSet()) {
            User user = person.getUser();
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
        return user != null && getUnitSite().getManagersSet().contains(user.getPerson());
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
        setUnitSite(null);
        super.gc();
    }

    public static PersistentManagersOfUnitSiteGroup getInstance(final UnitSite site) {
        PersistentManagersOfUnitSiteGroup instance = site.getManagersOfUnitSiteGroup();
        return instance != null ? instance : create(site);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentManagersOfUnitSiteGroup create(final UnitSite site) {
        PersistentManagersOfUnitSiteGroup instance = site.getManagersOfUnitSiteGroup();
        return instance != null ? instance : new PersistentManagersOfUnitSiteGroup(site);
    }
}
