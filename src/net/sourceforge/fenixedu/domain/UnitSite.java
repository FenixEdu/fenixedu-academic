package net.sourceforge.fenixedu.domain;

import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import dml.runtime.RelationAdapter;

public class UnitSite extends UnitSite_Base {
    
	private static MultiLanguageString TOP_SECTION_NAME = MultiLanguageString.i18n()
		.add("pt", "Topo")
		.add("en", "Top").finish();

	private static MultiLanguageString SIDE_SECTION_NAME = MultiLanguageString.i18n()
		.add("pt", "Lateral")
		.add("en", "Side").finish();
	
    static {
        UnitSiteManagers.addListener(new ManageWebsiteManagerRole());
    }
    
    protected UnitSite() {
        super();
        
        setShowInstitutionLogo(true);
        setShowFlags(true);
        setPersonalizedLogo(false);
        setShowIntroduction(true);
        setShowBanner(true);
        setShowAnnouncements(true);
        setShowEvents(true);
        
        new Section(this, TOP_SECTION_NAME);
        new Section(this, SIDE_SECTION_NAME);
    }

    public UnitSite(Unit unit) {
    	this();
    	
    	setUnit(unit);
    }
    
    @Override
    public IGroup getOwner() {
    	return new FixedSetGroup(getManagers());
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setDescription(MultiLanguageString description) {
        super.setDescription(description == null || description.isEmpty() ? null : description);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setSideBanner(MultiLanguageString sideBanner) {
        super.setSideBanner(sideBanner);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowAnnouncements(Boolean showAnnouncements) {
        super.setShowAnnouncements(showAnnouncements);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowEvents(Boolean showEvents) {
        super.setShowEvents(showEvents);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowBanner(Boolean showBanner) {
        super.setShowBanner(showBanner);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowIntroduction(Boolean showIntroduction) {
        super.setShowIntroduction(showIntroduction);
    }

    @Override
    @Checked("UnitSitePredicates.managers")
    public void setPersonalizedLogo(Boolean personalizedLogo) {
        super.setPersonalizedLogo(personalizedLogo);
    }
    
    @Override
    @Checked("UnitSitePredicates.managers")
    public void setShowFlags(Boolean showFlags) {
    	super.setShowFlags(showFlags);
    }
    
    public boolean isDefaultLogoUsed() {
    	Boolean bool = getPersonalizedLogo();
    	return (bool==null) ? true : !bool;
    }
    
    public void setDefaultLogoUsed(boolean bool) {
    	setPersonalizedLogo(!bool);
    }
    
    @Override
    public UnitSiteLayoutType getLayout() {
        UnitSiteLayoutType layout = super.getLayout();
        return layout == null ? UnitSiteLayoutType.BANNER_INTRO : layout;
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
    
    public boolean isBannerAvailable() {
    	Integer sum = null;
    	
    	List<UnitSiteBanner> banners = getBanners();
		for (UnitSiteBanner banner : banners) {
    		Integer weight = banner.getWeight();
    		
			if (weight != null) {
    			if (sum == null) {
    				sum = weight;
    			}
    			else {
    				sum += weight;
    			}
    		}
    	}
    	
    	return !banners.isEmpty() && (sum == null || sum > 0);
    }
    
    public UnitSiteBanner getCurrentBanner() {
        List<UnitSiteBanner> banners = getBanners();
        
        if (banners.isEmpty()) {
            return null;
        }

        boolean hasWeight = false;
        int sum = 0;
        int[] weights = new int[banners.size()];
        
        for (int i = 0; i < weights.length; i++) {
			Integer weight = banners.get(i).getWeight();
    		weights[i] = weight == null ? 0 : weight;
    		
    		hasWeight = hasWeight || weight != null;
    		sum += weights[i];
        }
        
        if (! hasWeight) {
        	return banners.get(new Random().nextInt(banners.size()));
        }
        else {
        	if (sum == 0) {
        		return null;
        	}
        	
        	int pos = new Random().nextInt(sum);
    		int partialSum = 0;
        	
        	for (int i = 0; i < weights.length; i++) {
        		partialSum += weights[i];
        		
        		if (partialSum > pos) {
        			return banners.get(i);
        		}
			}
        }
        
        return null;
    }
    
    @Override
    public boolean canBeDeleted() {
    	if (! super.canBeDeleted()) {
    		return false;
    	}
    	
    	if (hasAnyBanners()) {
    		return false;
    	}
    	
    	if (hasLogo()) {
    		return false;
    	}
    	
    	return true;
    }
    
    public Section getSideSection() {
		for (Section section : getAssociatedSections()) {
			if (isSideSection(section)) {
				return section;
			}
		}
		
		return null;
	}

	private boolean isSideSection(Section section) {
		String pt = section.getName().getContent(Language.pt);
		String en = section.getName().getContent(Language.en);
		
		return (pt != null && pt.equalsIgnoreCase("lateral")) || (en != null && en.equalsIgnoreCase("side"));
	}
    
    /**
     * Utility method to allow the user of an intenationalized name for the
     * site. Normally units, as a Party, don't have and internacionalized name
     * but some subclasses may override this behaviour. 
     * 
     * @return an internacionalized name
     */
    public MultiLanguageString getUnitNameWithAcronym() {
    	return new MultiLanguageString(getUnit().getNameWithAcronym());
    }

//    public Section getIntroductionSection() {
//    	if (!hasAnyIntroductionSections()) {
//    		return null;
//    	}
//    	else {
//    		return getIntroductionSections().iterator().next();
//    	}
//    }
//    
//    public void setIntroductionSection(Section section) {
//    	if (section == null) {
//    		getIntroductionSections().clear();
//    	}
//    	else {
//    		addIntroductionSections(section);
//    	}
//    }

    /**
     * Actually checks that the site template has a module associated to it.
     * 
     * 
     * @return getTemplate().hasModule() 
     */
//    public boolean isContextModuleAvailable() {
//	MetaDomainObjectPortal template = (MetaDomainObjectPortal) getTemplate();
//    	
//    	if (template == null) {
//    		return false;
//    	}
//    	
//    	return template.hasContainer() && !template.getContainer().getChildren(Functionality.class).isEmpty();
//    }
    
    @Override
    public void setGoogleAnalyticsCode(String code) {
        if (code == null || code.trim().length() == 0) {
            super.setGoogleAnalyticsCode(null);
        }
        else {
            super.setGoogleAnalyticsCode(code);
        }
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
    
    //TODO: Refactor this part in order to have relations instead of a naming convention.
    
    private Container getHardCodedContainers(MultiLanguageString name) {
	for(Content content : getChildrenAsContent()) {
	    if(content.getName().equalInAnyLanguage(name)) {
		return (Container)content;
	    }
	}
	return null;
    }
    
    public Container getSideContainer() {
	return getHardCodedContainers(this.SIDE_SECTION_NAME);
    }
    
    public Container getTopContainer() {
	return getHardCodedContainers(this.TOP_SECTION_NAME);
    }

}
