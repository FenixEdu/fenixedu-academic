package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class PunctualRoomsOccupationComment extends PunctualRoomsOccupationComment_Base {
    
    public static final Comparator<PunctualRoomsOccupationComment> COMPARATOR_BY_INSTANT = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("instant"));	
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("idInternal"));
    }
    
    public PunctualRoomsOccupationComment(PunctualRoomsOccupationRequest request, MultiLanguageString subject, 
	    MultiLanguageString description, Person owner, DateTime instant) {
        
	super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRequest(request);
        setOwner(owner);                    
        setSubject(subject);
        setDescription(description);
        setInstant(instant);               
    }  
    
    public void edit(MultiLanguageString subject, MultiLanguageString description) {
	if(!getRequest().getCurrentState().equals(RequestState.NEW)) {
	    throw new DomainException("error.PunctualRoomsOccupationRequest.impossible.edit");
	}	
	setSubject(subject);
	setDescription(description);
    }
    
    public String getPresentationInstant() {
	return getInstant().toString("dd/MM/yyyy HH:mm");
    }
    
    public RequestState getState() {
	return getRequest().getState(getInstant());
    }
    
    @Override
    public void setDescription(MultiLanguageString description) {	
	if(description == null || description.isEmpty()) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.description");
	}
	super.setDescription(description);
    }

    @Override
    public void setRequest(PunctualRoomsOccupationRequest request) {
	if(request == null) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.request");
	}
	super.setRequest(request);
    }

    @Override
    public void setOwner(Person owner) {
	if(owner == null) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.owner");
	}
	super.setOwner(owner);
    }      
}
