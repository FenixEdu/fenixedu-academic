package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentManagersOfUnitSiteGroup instance = site.getManagersOfUnitSiteGroup();
        return instance != null ? instance : create(site);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentManagersOfUnitSiteGroup create(final UnitSite site) {
        PersistentManagersOfUnitSiteGroup instance = site.getManagersOfUnitSiteGroup();
        return instance != null ? instance : new PersistentManagersOfUnitSiteGroup(site);
    }
}
