package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
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

        setRootDomainObject(RootDomainObject.getInstance());
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
    public boolean hasRootDomainObject() {
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
