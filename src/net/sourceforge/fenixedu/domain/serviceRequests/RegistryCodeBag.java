package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class RegistryCodeBag extends RegistryCodeBag_Base {
    static {
	InstitutionRegistryCodeGeneratorRegistryCodeBag
		.addListener(new RelationAdapter<RegistryCodeBag, InstitutionRegistryCodeGenerator>() {
		    @Override
		    public void beforeAdd(RegistryCodeBag bag, InstitutionRegistryCodeGenerator generator) {
			if (bag != null && generator != null && generator.hasCurrentBag()) {
			    bag.setPrevious(generator.getCurrentBag());
			}
		    }
		});
    }

    public RegistryCodeBag(InstitutionRegistryCodeGenerator registryCodeGenerator) {
	super();
	setDate(new DateTime());
	setRegistryCodeGenerator(registryCodeGenerator);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void closeBag() {
	if (hasRegistryCodeGenerator()) {
	    new RegistryCodeBag(getRegistryCodeGenerator());
	} else {
	    throw new DomainException("error.serviceRequests.RegistryCodeBag.attempting.to.close.past.bag");
	}
    }
}
