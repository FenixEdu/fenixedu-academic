package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class UnitSiteFile extends UnitSiteFile_Base {

    public UnitSiteFile() {
        super();
    }

    public UnitSiteFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
            byte[] content, Group group) {
        init(path, filename, displayName, metadata, content, group);
    }

    @Override
    public void delete() {
        setUnitSite(null);
        super.delete();
    }

    @Deprecated
    public boolean hasUnitSite() {
        return getUnitSite() != null;
    }

}
