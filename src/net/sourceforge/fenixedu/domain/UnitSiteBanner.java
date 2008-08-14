package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import dml.runtime.RelationAdapter;

public class UnitSiteBanner extends UnitSiteBanner_Base {

    private static final class CheckBannerAuthorization extends RelationAdapter<UnitSiteBanner, UnitSite> {
	@Override
	public void beforeAdd(UnitSiteBanner banner, UnitSite site) {
	    super.beforeAdd(banner, site);

	    if (banner != null && site != null) {
		if (!UnitSitePredicates.managers.evaluate(site)) {
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

    public float getWeightPercentage() {
	Integer weight = getWeight();
	List<UnitSiteBanner> banners = getSite().getBanners();

	if (weight == null) {
	    for (UnitSiteBanner banner : banners) {
		if (banner.getWeight() != null) {
		    return 0.0f;
		}
	    }

	    return 100f / banners.size();
	} else {
	    int total = 0;
	    for (UnitSiteBanner banner : banners) {
		Integer w = banner.getWeight();

		if (w != null) {
		    total += w;
		}
	    }

	    return weight * 100f / total;
	}

    }
}
