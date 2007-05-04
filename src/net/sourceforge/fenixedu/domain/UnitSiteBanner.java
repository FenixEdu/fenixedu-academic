package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import dml.runtime.RelationAdapter;

public class UnitSiteBanner extends UnitSiteBanner_Base {
    
	private static final class CheckBannerAuthorization extends RelationAdapter<UnitSiteBanner, UnitSite> {
		@Override
		public void beforeAdd(UnitSiteBanner banner, UnitSite site) {
			super.beforeAdd(banner, site);
			
			if (banner != null && site != null) {
				if (! UnitSitePredicates.managers.evaluate(site)) {
					throw new DomainException("unitSite.link.notAuthorized");
				}
			}
		}
	}
	
	static {
		UnitSiteHasBanners.addListener(new CheckBannerAuthorization());
	}
	
    protected UnitSiteBanner() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public UnitSiteBanner(UnitSite site) {
        this();
        
        setSite(site);
    }
    
    @Override
    public void setLink(String link) {
    	super.setLink(link);
    }
    
    @Override
    public void setColor(String color) {
    	super.setColor(color);
    }
    
    @Override
    public void setWeight(Integer weight) {
    	super.setWeight(weight);
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
