package net.sourceforge.fenixedu.domain.system;

import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CronRegistry extends CronRegistry_Base {

    public CronRegistry() {
	super();
	if (getInstance() != null) {
	    throw new DomainException("error.cron.registry.instance.exists");
	}
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static CronRegistry getInstance() {
	final Set<CronRegistry> cronRegistries = RootDomainObject.getInstance().getCronRegistrySet();
	return cronRegistries.isEmpty() ? null : cronRegistries.iterator().next();
    }

}
