package net.sourceforge.fenixedu.domain.protocols;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import net.sourceforge.fenixedu.domain.accessControl.Group;


public class ProtocolFile extends ProtocolFile_Base {
    
    public  ProtocolFile() {
        super();
        setOjbConcreteClass(ProtocolFile.class.getName());        
    }
    
    public ProtocolFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
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
