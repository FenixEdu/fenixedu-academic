package net.sourceforge.fenixedu.domain.tests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class PictureMaterialFile extends PictureMaterialFile_Base {

    public PictureMaterialFile() {
        super();
    }

    public PictureMaterialFile(final VirtualPath virtualPath, String filename, String displayName,
            Collection<FileSetMetaData> metadata, byte[] content, Group permittedGroup) {
        this();
        init(virtualPath, filename, displayName, metadata, content, permittedGroup);
    }

    public void delete(NewPictureMaterial pictureMaterial) {
        this.removePictureMaterials(pictureMaterial);

        if (this.getPictureMaterialsCount() == 0) {
            this.removeRootDomainObject();

            super.deleteDomainObject();
        }
    }

}
