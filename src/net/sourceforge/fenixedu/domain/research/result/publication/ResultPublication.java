package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public abstract class ResultPublication extends ResultPublication_Base {

    public enum ScopeType {
	LOCAL, NATIONAL, INTERNATIONAL;
    }
    
    /**
     * Comparator than can be used to order publications by Year.
     */
    static class OrderComparator implements Comparator<ResultPublication> {
	public int compare(ResultPublication rp1, ResultPublication rp2) {
	    Integer pub1 = rp1.getYear();
            Integer pub2 = rp2.getYear();
            if (pub1 == null) {
                return 1;
            } else if (pub2 == null) {
                return -1;
            }
            return (-1) * pub1.compareTo(pub2);
	}
    }
    
    public static <T extends ResultPublication> List<T> sort(Collection<T> resultPublications) {
	List<T> sorted = new ArrayList<T>(resultPublications);
	Collections.sort(sorted, new ResultPublication.OrderComparator());

	return sorted;
    }

    public ResultPublication() {
	super();
    }
    
    public List<ResultParticipation> getAuthors() {
	ArrayList<ResultParticipation> authors = new ArrayList<ResultParticipation>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Author))
		authors.add(participation);
	}
	return authors;
    }

    public List<ResultParticipation> getEditors() {
	ArrayList<ResultParticipation> editors = new ArrayList<ResultParticipation>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Editor))
		editors.add(participation);
	}
	return editors;
    }
    
    private void removeAssociations() {
	super.setPublisher(null);
	super.setOrganization(null);
    }

    @Override
    @Checked("ResultPredicates.writePredicate")
    public void delete() {
	removeAssociations();
	super.delete();
    }
    
    @Override
    public void removePublisher() {
	throw new DomainException("error.researcher.ResultPublication.call","removePublisher");
    }

    @Override
    public void removeOrganization() {
	throw new DomainException("error.researcher.ResultPublication.call","removeOrganization");
    }
}
