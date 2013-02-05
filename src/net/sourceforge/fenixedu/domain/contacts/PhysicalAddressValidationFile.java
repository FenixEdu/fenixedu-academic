package net.sourceforge.fenixedu.domain.contacts;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhysicalAddressValidationFile extends PhysicalAddressValidationFile_Base {

    public PhysicalAddressValidationFile(PhysicalAddressValidation validation, String filename, String displayName, byte[] content) {
        super();
        super.init(obtainVirtualPath(), filename, displayName, Collections.EMPTY_LIST, content, new RoleGroup(RoleType.OPERATOR));
        setPyhsicalAddressValidation(validation);
    }

    private VirtualPath obtainVirtualPath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("PhysicalAddressValidationFile", "PhysicalAddressValidationFile files"));
        filePath.addNode(new VirtualPathNode("PhysicalAddressValidationFile" + getExternalId(),
                "PhysicalAddressValidationFile files" + getExternalId()));
        return filePath;
    }

}
