package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class UnitSiteBannerFile extends UnitSiteBannerFile_Base {

    public UnitSiteBannerFile() {
        super();
    }

    public UnitSiteBannerFile(String filename, String displayName, byte[] content, Group group) {
        this();
        init(filename, displayName, content, group);
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
