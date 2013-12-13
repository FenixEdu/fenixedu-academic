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
        if (this.getPictureMaterialsSet().size() == 0) {
            super.delete();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewPictureMaterial> getPictureMaterials() {
        return getPictureMaterialsSet();
    }

    @Deprecated
    public boolean hasAnyPictureMaterials() {
        return !getPictureMaterialsSet().isEmpty();
    }

}
