package net.sourceforge.fenixedu.domain.protocols;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class ProtocolFile extends ProtocolFile_Base {

    public ProtocolFile() {
        super();
        setOjbConcreteClass(getClass().getName());
    }

    public ProtocolFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }

    public String getFilePermissionType() {
        if (getPermittedGroup() instanceof InternalPersonGroup) {
            return FilePermissionType.IST_PEOPLE.toString();
        } else {
            return FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL.toString();
        }
    }

    public void delete() {
        setRootDomainObject(null);
        setProtocol(null);
        setPermittedGroup(null);
        String externalIdentifier = getExternalStorageIdentification();
        FileManagerFactory.getFactoryInstance().getFileManager().deleteFile(externalIdentifier);
        deleteDomainObject();
    }
}
