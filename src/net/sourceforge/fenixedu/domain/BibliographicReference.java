package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;

public class BibliographicReference extends BibliographicReference_Base {

    public static final Comparator<BibliographicReference> COMPARATOR_BY_ORDER = new Comparator<BibliographicReference>() {

        private ComparatorChain chain = null;
        
        public int compare(BibliographicReference one, BibliographicReference other) {
            if (this.chain == null) {
                chain = new ComparatorChain();
                
                chain.addComparator(new BeanComparator("referenceOrder", new NullComparator(true)));
                chain.addComparator(new BeanComparator("title"));
                chain.addComparator(new BeanComparator("year"));
                chain.addComparator(DomainObject.COMPARATOR_BY_ID);
            }
            
            return chain.compare(one, other);
        }
    };
    
    public BibliographicReference() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void edit(final String title, final String authors, final String reference,
	    final String year, final Boolean optional) {
	if (title == null || authors == null || reference == null || year == null || optional == null)
	    throw new NullPointerException();

	setTitle(title);
	setAuthors(authors);
	setReference(reference);
	setYear(year);
	setOptional(optional);
    }

    public void delete() {
	removeExecutionCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isOptional() {
        return getOptional() == null || getOptional();
    }
}
