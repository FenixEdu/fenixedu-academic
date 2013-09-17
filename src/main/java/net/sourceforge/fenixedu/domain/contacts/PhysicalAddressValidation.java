package net.sourceforge.fenixedu.domain.contacts;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public class PhysicalAddressValidation extends PhysicalAddressValidation_Base {

    public static final Predicate<PartyContactValidation> PREDICATE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean eval(PartyContactValidation t) {
            return t instanceof PhysicalAddressValidation;
        }

    };

    public static final Predicate<PartyContactValidation> PREDICATE_FILE = new Predicate<PartyContactValidation>() {

        @Override
        public boolean eval(PartyContactValidation t) {
            return PREDICATE.eval(t) && ((PhysicalAddressValidation) t).hasFile();
        }

    };

    public PhysicalAddressValidation(PhysicalAddress physicalAddress) {
        super();
        setPartyContact(physicalAddress);
    }

    @Atomic
    public void setFile(String filename, String displayName, byte[] content) {
        new PhysicalAddressValidationFile(this, filename, displayName, content);
    }
    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

}
