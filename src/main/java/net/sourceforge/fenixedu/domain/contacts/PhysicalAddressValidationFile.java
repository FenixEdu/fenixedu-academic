package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PhysicalAddressValidationFile extends PhysicalAddressValidationFile_Base {

    public PhysicalAddressValidationFile(PhysicalAddressValidation validation, String filename, String displayName, byte[] content) {
        super();
        super.init(filename, displayName, content, new RoleGroup(RoleType.OPERATOR));
        setPyhsicalAddressValidation(validation);
    }

    @Deprecated
    public boolean hasPyhsicalAddressValidation() {
        return getPyhsicalAddressValidation() != null;
    }

}
