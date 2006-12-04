package net.sourceforge.fenixedu.domain.parking;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ParkingFile extends ParkingFile_Base {

    public static final long MAX_FILE_SIZE = 3145728; //3MB

    public ParkingFile() {
        super();
        setOjbConcreteClass(ParkingFile.class.getName());
    }

    public ParkingFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);

    }

    public void delete() {
        setRootDomainObject(null);
        setNewParkingDocument(null);
        setPermittedGroup(null);
        String externalIdentifier = getExternalStorageIdentification();
        FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
        deleteDomainObject();
    }
}
