package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class UnitSiteBannerFile extends UnitSiteBannerFile_Base {

    public UnitSiteBannerFile() {
        super();
    }

    public UnitSiteBannerFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
            byte[] content, Group group) {
        this();
        init(path, filename, displayName, metadata, content, group);
    }

    @Override
    public void delete() {
        setBackgroundBanner(null);
        setMainBanner(null);
        super.delete();
    }

    @Deprecated
    public boolean hasMainBanner() {
        return getMainBanner() != null;
    }

    @Deprecated
    public boolean hasBackgroundBanner() {
        return getBackgroundBanner() != null;
    }

}
