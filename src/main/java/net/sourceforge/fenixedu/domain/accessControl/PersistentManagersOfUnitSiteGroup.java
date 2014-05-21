package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentManagersOfUnitSiteGroup extends PersistentManagersOfUnitSiteGroup_Base {
    protected PersistentManagersOfUnitSiteGroup(UnitSite site) {
        super();
        setUnitSite(site);
    }

    @Override
    public Group toGroup() {
        return ManagersOfUnitSiteGroup.get(getUnitSite());
    }

    @Override
    protected void gc() {
        setUnitSite(null);
        super.gc();
    }

    public static PersistentManagersOfUnitSiteGroup getInstance(final UnitSite site) {
        return singleton(() -> Optional.ofNullable(site.getManagersOfUnitSiteGroup()),
                () -> new PersistentManagersOfUnitSiteGroup(site));
    }
}
