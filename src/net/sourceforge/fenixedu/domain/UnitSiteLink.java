package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.util.domain.InverseOrderedRelationAdapter;

public class UnitSiteLink extends UnitSiteLink_Base {
    
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
    
    public void delete() {
        removeRootDomainObject();
        removeTopUnitSite();
        removeFooterUnitSite();
        
        deleteDomainObject();
    }
    
}
