package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ParkingFile extends ParkingFile_Base {

    public static final long MAX_FILE_SIZE = 3145728; // 3MB

    public ParkingFile() {
        super();
    }

    public ParkingFile(String filename, String displayName, byte[] content, Group permittedGroup) {
        this();
        init(filename, displayName, content, permittedGroup);
    }

    @Override
    public void delete() {
        setNewParkingDocument(null);
        super.delete();
    }

    @Deprecated
    public boolean hasNewParkingDocument() {
        return getNewParkingDocument() != null;
    }

}
