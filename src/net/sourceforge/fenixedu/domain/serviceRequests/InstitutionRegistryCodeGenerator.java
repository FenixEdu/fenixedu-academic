package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {
    public InstitutionRegistryCodeGenerator() {
	super();
	new RegistryCodeBag(this);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected Integer getNextNumber() {
	super.setCurrent((super.getCurrent() != null ? super.getCurrent() : 0) + 1);
	return super.getCurrent();
    }

    @Override
    public Integer getCurrent() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrent(Integer current) {
	throw new UnsupportedOperationException();
    }

    public List<RegistryCodeBag> getRegistryBags() {
	LinkedList<RegistryCodeBag> result = new LinkedList<RegistryCodeBag>();
	do {
	    result.addLast(getCurrentBag());
	} while (getCurrentBag().hasPrevious());
	return result;
    }

    public RegistryCode createRegistryFor(RegistryDiplomaRequest request) {
	return new RegistryDiplomaCode(this, request);
    }

    public RegistryCode createRegistryFor(DiplomaRequest request) {
	return new DiplomaCode(this, request);
    }
}
