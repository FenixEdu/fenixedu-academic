package net.sourceforge.fenixedu.domain;

public class UnitSiteFile extends UnitSiteFile_Base {
    
    public UnitSiteFile() {
        super();
    }

    public UnitSiteFile(String uniqueId, String name) {
        init(name, name, null, null, null, null, uniqueId, null);
    }

    public void delete() {
        removeRootDomainObject();
        removeUnitSite();

        deleteDomainObject();
    }
    
}
