package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class UnitSiteFile extends UnitSiteFile_Base {

    public UnitSiteFile() {
        super();
    }

    public UnitSiteFile(String filename, String displayName, byte[] content, Group group) {
        init(filename, displayName, content, group);
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
