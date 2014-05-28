/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class UnitSiteBanner extends UnitSiteBanner_Base {

    private static final class CheckBannerAuthorization extends RelationAdapter<UnitSite, UnitSiteBanner> {
        @Override
        public void beforeAdd(UnitSite site, UnitSiteBanner banner) {
            super.beforeAdd(site, banner);

            if (banner != null && site != null) {
                if (!UnitSitePredicates.managers.evaluate(site)) {
                    throw new DomainException("unitSite.link.notAuthorized");
                }
            }
        }
    }

    static {
        getRelationUnitSiteHasBanners().addListener(new CheckBannerAuthorization());
    }

    protected UnitSiteBanner() {
        super();

        setRootDomainObject(Bennu.getInstance());
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
        if (getMainImage() != null) {
            getMainImage().delete();
        }
        if (getBackgroundImage() != null) {
            getBackgroundImage().delete();
        }
        setSite(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public float getWeightPercentage() {
        Integer weight = getWeight();
        Collection<UnitSiteBanner> banners = getSite().getBanners();

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

    @Deprecated
    public boolean hasBackgroundImage() {
        return getBackgroundImage() != null;
    }

    @Deprecated
    public boolean hasSite() {
        return getSite() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLink() {
        return getLink() != null;
    }

    @Deprecated
    public boolean hasWeight() {
        return getWeight() != null;
    }

    @Deprecated
    public boolean hasMainImage() {
        return getMainImage() != null;
    }

    @Deprecated
    public boolean hasRepeatType() {
        return getRepeatType() != null;
    }

    @Deprecated
    public boolean hasColor() {
        return getColor() != null;
    }

}
