package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.predicates.UnitSitePredicates;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import net.sourceforge.fenixedu.util.domain.InverseOrderedRelationAdapter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import dml.runtime.RelationAdapter;

public class UnitSiteLink extends UnitSiteLink_Base {

    private static final class CheckLinkAuthorization extends RelationAdapter<UnitSiteLink, UnitSite> {
	@Override
	public void beforeAdd(UnitSiteLink link, UnitSite site) {
	    super.beforeAdd(link, site);

	    if (link != null && site != null) {
		if (!UnitSitePredicates.managers.evaluate(site)) {
		    throw new DomainException("unitSite.link.notAuthorized");
		}
	    }
	}
    }

    static {
	UnitSiteTopLinks.addListener(new CheckLinkAuthorization());
	UnitSiteFooterLinks.addListener(new CheckLinkAuthorization());
    }

    public static Comparator<UnitSiteLink> COMPARATOR_BY_ORDER = new Comparator<UnitSiteLink>() {
	private ComparatorChain chain = null;

	public int compare(UnitSiteLink o1, UnitSiteLink o2) {
	    if (this.chain == null) {
		chain = new ComparatorChain();

		chain.addComparator(new BeanComparator("linkOrder"));
		chain.addComparator(new BeanComparator("label.content"));
		chain.addComparator(DomainObject.COMPARATOR_BY_ID);
	    }

	    return chain.compare(o1, o2);
	}

    };

    public static InverseOrderedRelationAdapter<UnitSiteLink, UnitSite> TOP_ORDER_ADAPTER;
    public static InverseOrderedRelationAdapter<UnitSiteLink, UnitSite> FOOTER_ORDER_ADAPTER;

    static {
	TOP_ORDER_ADAPTER = new InverseOrderedRelationAdapter<UnitSiteLink, UnitSite>("linkOrder", "topLinks");
	FOOTER_ORDER_ADAPTER = new InverseOrderedRelationAdapter<UnitSiteLink, UnitSite>("linkOrder", "footerLinks");

	UnitSiteTopLinks.addListener(TOP_ORDER_ADAPTER);
	UnitSiteFooterLinks.addListener(FOOTER_ORDER_ADAPTER);
    }

    public UnitSiteLink() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    @Checked("UnitSitePredicates.linkSiteManagers")
    public void setUrl(String url) {
	super.setUrl(url);
    }

    @Override
    @Checked("UnitSitePredicates.linkSiteManagers")
    public void setLabel(MultiLanguageString label) {
	super.setLabel(label);
    }

    public void delete() {
	removeRootDomainObject();
	removeTopUnitSite();
	removeFooterUnitSite();

	deleteDomainObject();
    }

}
