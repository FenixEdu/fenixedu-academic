package net.sourceforge.fenixedu.domain.contacts;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public class PhysicalAddressValidation extends PhysicalAddressValidation_Base {

    public static final Predicate<PartyContactValidation> PREDICATE = new Predicate<PartyContactValidation>() {

	@Override
	public boolean eval(PartyContactValidation t) {
	    return t instanceof PhysicalAddressValidation;
	}

    };

    public PhysicalAddressValidation(PhysicalAddress physicalAddress) {
	super();
	setPartyContact(physicalAddress);
    }

    @Service
    public void setFile(String filename, String displayName, byte[] content) {
	new PhysicalAddressValidationFile(this, filename, displayName, content);
    }
}
