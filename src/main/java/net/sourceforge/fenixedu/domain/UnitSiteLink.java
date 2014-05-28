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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class UnitSiteLink extends UnitSiteLink_Base {

    private static final class CheckLinkAuthorization extends RelationAdapter<UnitSite, UnitSiteLink> {
        @Override
        public void beforeAdd(UnitSite site, UnitSiteLink link) {
            super.beforeAdd(site, link);

            if (link != null && site != null) {
                if (!UnitSitePredicates.managers.evaluate(site)) {
                    throw new DomainException("unitSite.link.notAuthorized");
                }
            }
        }
    }

    static {
        getRelationUnitSiteTopLinks().addListener(new CheckLinkAuthorization());
        getRelationUnitSiteFooterLinks().addListener(new CheckLinkAuthorization());
    }

    public static Comparator<UnitSiteLink> COMPARATOR_BY_ORDER = new Comparator<UnitSiteLink>() {
        private ComparatorChain chain = null;

        @Override
        public int compare(UnitSiteLink o1, UnitSiteLink o2) {
            if (this.chain == null) {
                chain = new ComparatorChain();

                chain.addComparator(new BeanComparator("linkOrder"));
                chain.addComparator(new BeanComparator("label.content"));
                chain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
            }

            return chain.compare(o1, o2);
        }

    };

    public static OrderedRelationAdapter<UnitSite, UnitSiteLink> TOP_ORDER_ADAPTER;
    public static OrderedRelationAdapter<UnitSite, UnitSiteLink> FOOTER_ORDER_ADAPTER;

    static {
        TOP_ORDER_ADAPTER = new OrderedRelationAdapter<UnitSite, UnitSiteLink>("linkOrder", "topLinks");
        FOOTER_ORDER_ADAPTER = new OrderedRelationAdapter<UnitSite, UnitSiteLink>("linkOrder", "footerLinks");

        getRelationUnitSiteTopLinks().addListener(TOP_ORDER_ADAPTER);
        getRelationUnitSiteFooterLinks().addListener(FOOTER_ORDER_ADAPTER);
    }

    public UnitSiteLink() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    public void setUrl(String url) {
        check(this, UnitSitePredicates.linkSiteManagers);
        super.setUrl(url);
    }

    @Override
    public void setLabel(MultiLanguageString label) {
        check(this, UnitSitePredicates.linkSiteManagers);
        super.setLabel(label);
    }

    public void delete() {
        setRootDomainObject(null);
        setTopUnitSite(null);
        setFooterUnitSite(null);

        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasFooterUnitSite() {
        return getFooterUnitSite() != null;
    }

    @Deprecated
    public boolean hasLabel() {
        return getLabel() != null;
    }

    @Deprecated
    public boolean hasLinkOrder() {
        return getLinkOrder() != null;
    }

    @Deprecated
    public boolean hasTopUnitSite() {
        return getTopUnitSite() != null;
    }

}
