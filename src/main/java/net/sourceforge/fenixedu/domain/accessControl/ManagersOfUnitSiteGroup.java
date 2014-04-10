package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("siteManagers")
public class ManagersOfUnitSiteGroup extends FenixGroup {
    private static final long serialVersionUID = 1045771762725375319L;

    @GroupArgument
    private UnitSite site;

    private ManagersOfUnitSiteGroup() {
        super();
    }

    private ManagersOfUnitSiteGroup(UnitSite site) {
        this();
        this.site = site;
    }

    public static ManagersOfUnitSiteGroup get(UnitSite site) {
        return new ManagersOfUnitSiteGroup(site);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { site.getUnit().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : site.getManagersSet()) {
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
        return user != null && site.getManagersSet().contains(user.getPerson());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentManagersOfUnitSiteGroup.getInstance(site);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ManagersOfUnitSiteGroup) {
            return Objects.equal(site, ((ManagersOfUnitSiteGroup) object).site);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(site);
    }

}
