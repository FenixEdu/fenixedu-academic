package net.sourceforge.fenixedu.domain;

public class UnitSiteBannerFile extends UnitSiteBannerFile_Base {
    
    public UnitSiteBannerFile() {
        super();
    }

    public UnitSiteBannerFile(String uniqueId, String name) {
        this();
        
        init(name, name, null, null, null, null, uniqueId, null);
    }

    public void delete() {
        removeRootDomainObject();
        removeBackgroundBanner();
        removeMainBanner();
        
        deleteDomainObject();
    }
    
}
