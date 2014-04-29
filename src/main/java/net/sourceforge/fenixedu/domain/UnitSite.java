package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class UnitSite extends UnitSite_Base {

    private static MultiLanguageString TOP_SECTION_NAME = new MultiLanguageString().with(Language.pt, "Topo").with(Language.en,
            "Top");

    private static MultiLanguageString SIDE_SECTION_NAME = new MultiLanguageString().with(Language.pt, "Lateral").with(
            Language.en, "Side");

    static {
        getRelationUnitSiteManagers().addListener(new ManageWebsiteManagerRole());
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
    public void setDescription(MultiLanguageString description) {
        check(this, UnitSitePredicates.managers);
        super.setDescription(description == null || description.isEmpty() ? null : description);
    }

    @Override
    public void setSideBanner(MultiLanguageString sideBanner) {
        check(this, UnitSitePredicates.managers);
        super.setSideBanner(sideBanner);
    }

    @Override
    public void setShowAnnouncements(Boolean showAnnouncements) {
        check(this, UnitSitePredicates.managers);
        super.setShowAnnouncements(showAnnouncements);
    }

    @Override
    public void setShowEvents(Boolean showEvents) {
        check(this, UnitSitePredicates.managers);
        super.setShowEvents(showEvents);
    }

    @Override
    public void setShowBanner(Boolean showBanner) {
        check(this, UnitSitePredicates.managers);
        super.setShowBanner(showBanner);
    }

    @Override
    public void setShowIntroduction(Boolean showIntroduction) {
        check(this, UnitSitePredicates.managers);
        super.setShowIntroduction(showIntroduction);
    }

    @Override
    public void setPersonalizedLogo(Boolean personalizedLogo) {
        check(this, UnitSitePredicates.managers);
        super.setPersonalizedLogo(personalizedLogo);
    }

    @Override
    public void setShowFlags(Boolean showFlags) {
        check(this, UnitSitePredicates.managers);
        super.setShowFlags(showFlags);
    }

    public boolean isDefaultLogoUsed() {
        Boolean bool = getPersonalizedLogo();
        return (bool == null) ? true : !bool;
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
        } else {
            return getUnit().getDefaultWebAddressUrl();
        }
    }

    @Override
    public boolean isDeletable() {
        return super.isDeletable() && !hasAnyBanners() && !hasLogo() && getManagersOfUnitSiteGroup() == null;
    }

    @Override
    protected void disconnect() {
        deleteLinks(getTopLinksSet());
        for (final Person person : getManagersSet()) {
            removeManagers(person);
        }
        setUnit(null);
        deleteLinks(getFooterLinksSet());
        super.disconnect();
    }

    protected void deleteLinks(final Set<UnitSiteLink> unitSiteLinks) {
        for (final UnitSiteLink unitSiteLink : unitSiteLinks) {
            unitSiteLink.delete();
        }
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

        Collection<UnitSiteBanner> banners = getBanners();
        for (UnitSiteBanner banner : banners) {
            Integer weight = banner.getWeight();

            if (weight != null) {
                if (sum == null) {
                    sum = weight;
                } else {
                    sum += weight;
                }
            }
        }

        return !banners.isEmpty() && (sum == null || sum > 0);
    }

    public UnitSiteBanner getCurrentBanner() {
        List<UnitSiteBanner> banners = new ArrayList<>(getBanners());

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

        if (!hasWeight) {
            return banners.get(new Random().nextInt(banners.size()));
        } else {
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

    // public Section getIntroductionSection() {
    // if (!hasAnyIntroductionSections()) {
    // return null;
    // }
    // else {
    // return getIntroductionSections().iterator().next();
    // }
    // }
    //
    // public void setIntroductionSection(Section section) {
    // if (section == null) {
    // getIntroductionSections().clear();
    // }
    // else {
    // addIntroductionSections(section);
    // }
    // }

    /**
     * Actually checks that the site template has a module associated to it.
     * 
     * 
     * @return getTemplate().hasModule()
     */
    // public boolean isContextModuleAvailable() {
    // MetaDomainObjectPortal template = (MetaDomainObjectPortal) getTemplate();
    //
    // if (template == null) {
    // return false;
    // }
    //
    // return template.hasContainer() &&
    // !template.getContainer().getChildren(Functionality.class).isEmpty();
    // }
    @Override
    public void setGoogleAnalyticsCode(String code) {
        if (code == null || code.trim().length() == 0) {
            super.setGoogleAnalyticsCode(null);
        } else {
            super.setGoogleAnalyticsCode(code);
        }
    }

    /**
     * Manage the role WEBSITE_MANAGER associated with the person. The person
     * becomes a WEBSITE_MANAGER when it's added as a manager of a UnitSite.
     * This listerner also removes the role from the person when it's no longer
     * a manager of some UnitSite.
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

    // TODO: Refactor this part in order to have relations instead of a naming
    // convention.

    private Container getHardCodedContainers(MultiLanguageString name) {
        for (Content content : getChildrenAsContent()) {
            if (content.getName().equalInAnyLanguage(name)) {
                return (Container) content;
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

    @Override
    public boolean isUnitSite() {
        return true;
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        groups.add(new WebSiteManagersGroup(this));
        groups.addAll(getUnit().getGroups());

        return groups;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitSiteBanner> getBanners() {
        return getBannersSet();
    }

    @Deprecated
    public boolean hasAnyBanners() {
        return !getBannersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitSiteLink> getTopLinks() {
        return getTopLinksSet();
    }

    @Deprecated
    public boolean hasAnyTopLinks() {
        return !getTopLinksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getManagers() {
        return getManagersSet();
    }

    @Deprecated
    public boolean hasAnyManagers() {
        return !getManagersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitSiteLink> getFooterLinks() {
        return getFooterLinksSet();
    }

    @Deprecated
    public boolean hasAnyFooterLinks() {
        return !getFooterLinksSet().isEmpty();
    }

    @Deprecated
    public boolean hasGoogleAnalyticsCode() {
        return getGoogleAnalyticsCode() != null;
    }

    @Deprecated
    public boolean hasShowFlags() {
        return getShowFlags() != null;
    }

    @Deprecated
    public boolean hasShowBanner() {
        return getShowBanner() != null;
    }

    @Deprecated
    public boolean hasLayout() {
        return getLayout() != null;
    }

    @Deprecated
    public boolean hasShowEvents() {
        return getShowEvents() != null;
    }

    @Deprecated
    public boolean hasLogo() {
        return getLogo() != null;
    }

    @Deprecated
    public boolean hasShowInstitutionLogo() {
        return getShowInstitutionLogo() != null;
    }

    @Deprecated
    public boolean hasShowIntroduction() {
        return getShowIntroduction() != null;
    }

    @Deprecated
    public boolean hasPersonalizedLogo() {
        return getPersonalizedLogo() != null;
    }

    @Deprecated
    public boolean hasSideBanner() {
        return getSideBanner() != null;
    }

    @Deprecated
    public boolean hasShowAnnouncements() {
        return getShowAnnouncements() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
