package net.sourceforge.fenixedu.domain;

public class UnitSiteBanner extends UnitSiteBanner_Base {
    
    protected UnitSiteBanner() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public UnitSiteBanner(UnitSite site) {
        this();
        
        setSite(site);
    }
    
    public void delete() {
        removeRootDomainObject();
        removeSite();
        
        if (getMainImage() != null) {
            getMainImage().delete();
        }
             
        if (getBackgroundImage() != null) {
            getBackgroundImage().delete();
        }
        
        deleteDomainObject();
    }
}
