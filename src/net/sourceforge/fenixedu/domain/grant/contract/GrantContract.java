package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantContract extends GrantContract_Base {

	public GrantContract() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public Set<GrantSubsidy> findGrantSubsidiesByState(final Integer state) {
        final Set<GrantSubsidy> grantSubsidies = new HashSet<GrantSubsidy>();
        for (final GrantSubsidy grantSubsidy : getAssociatedGrantSubsidiesSet()) {
            grantSubsidies.add(grantSubsidy);
        }
        return grantSubsidies;
    }

}
