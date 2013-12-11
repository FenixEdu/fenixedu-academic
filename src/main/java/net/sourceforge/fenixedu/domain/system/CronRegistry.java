package net.sourceforge.fenixedu.domain.system;

import java.util.Set;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CronRegistry extends CronRegistry_Base {

    public CronRegistry() {
        super();
        if (getInstance() != null) {
            throw new DomainException("error.cron.registry.instance.exists");
        }
        setRootDomainObject(Bennu.getInstance());
    }

    public static CronRegistry getInstance() {
        final Set<CronRegistry> cronRegistries = Bennu.getInstance().getCronRegistrySet();
        return cronRegistries.isEmpty() ? null : cronRegistries.iterator().next();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBuildVersion() {
        return getBuildVersion() != null;
    }

}
