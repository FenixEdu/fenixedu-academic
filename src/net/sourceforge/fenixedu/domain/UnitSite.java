package net.sourceforge.fenixedu.domain;

import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.person.RoleType;
import dml.runtime.RelationAdapter;

public abstract class UnitSite extends UnitSite_Base {
    
    static {
        UnitSiteManagers.addListener(new ManageWebsiteManagerRole());
    }
    
    public UnitSite() {
        super();
        
        setPersonalizedLogo(false);
        setShowIntroduction(true);
        setShowBanner(true);
        setShowAnnouncements(true);
        setShowEvents(true);
    }
    
    @Override
    public String getAlternativeSite() {
        String address = super.getAlternativeSite();
        if (address != null) {
            return address;
        }
        else {
            return getUnit().getWebAddress();
        }
    }

    @Override
    protected void deleteRelations() {
        super.deleteRelations();
        
        removeUnit();
    }

    public void setTopLinksOrder(List<UnitSiteLink> links) {
        UnitSiteLink.TOP_ORDER_ADAPTER.updateOrder(this, links);
    }

    public SortedSet<UnitSiteLink> getSortedTopLinks() {
        SortedSet<UnitSiteLink> sorted = new TreeSet<UnitSiteLink>(UnitSiteLink.COMPARATOR_BY_ORDER);
        sorted.addAll(getTopLinks());
        
        return sorted;
    }
    
    public void setFooterLinksOrder(List<UnitSiteLink> links) {
        UnitSiteLink.FOOTER_ORDER_ADAPTER.updateOrder(this, links);
    }

    public SortedSet<UnitSiteLink> getSortedFooterLinks() {
        SortedSet<UnitSiteLink> sorted = new TreeSet<UnitSiteLink>(UnitSiteLink.COMPARATOR_BY_ORDER);
        sorted.addAll(getFooterLinks());
        
        return sorted;
    }
    
    public UnitSiteBanner getCurrentBanner() {
        List<UnitSiteBanner> banners = getBanners();
        
        if (banners.isEmpty()) {
            return null;
        }
        
        return banners.get(new Random().nextInt() % banners.size());
    }
    
    /**
     * Manage the role WEBSITE_MANAGER associated with the person. The person
     * becomes a WEBSITE_MANAGER when it's added as a manager of a UnitSite. This
     * listerner also removes the role from the person when it's no longer a manager
     * of some UnitSite.
     * 
     * @author cfgi
     */
    private static class ManageWebsiteManagerRole extends RelationAdapter<UnitSite, Person> {
        
        @Override
        public void afterAdd(UnitSite site, Person person) {
            super.afterAdd(site, person);
            
            if (person != null && site != null) {
                person.addPersonRoleByRoleType(RoleType.WEBSITE_MANAGER);
            }
        }

        @Override
        public void afterRemove(UnitSite site, Person person) {
            super.afterRemove(site, person);
            
            if (person != null && site != null) {
                if (person.hasAnyUnitSites()) {
                    return;
                }
                
                person.removeRoleByType(RoleType.WEBSITE_MANAGER);
            }
        }
        
    }

}
