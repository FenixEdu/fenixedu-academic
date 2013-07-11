package net.sourceforge.fenixedu.domain.parking;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class ParkingFile extends ParkingFile_Base {

    public static final long MAX_FILE_SIZE = 3145728; // 3MB

    public ParkingFile() {
        super();
    }

    public ParkingFile(VirtualPath virtualPath, String filename, String displayName, byte[] content, Group permittedGroup) {
        this();
        init(virtualPath, filename, displayName, Collections.EMPTY_SET, content, permittedGroup);
    }

    @Override
    public void delete() {
        setNewParkingDocument(null);
        super.delete();
    }
}
