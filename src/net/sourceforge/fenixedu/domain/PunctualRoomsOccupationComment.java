package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

public class PunctualRoomsOccupationComment extends PunctualRoomsOccupationComment_Base {
    
    public static final Comparator<PunctualRoomsOccupationComment> COMPARATOR_BY_INSTANT = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("instant"));	
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManagePunctualRoomsOccupationComment")
    public PunctualRoomsOccupationComment(PunctualRoomsOccupationRequest request, MultiLanguageString subject, 
	    MultiLanguageString description, Person owner, DateTime instant) {
        
	super();
	checkIfCommentAlreadyExists(owner, subject, description);
        setRootDomainObject(RootDomainObject.getInstance());
        setRequest(request);
        setOwner(owner);                    
        setSubject(subject);
        setDescription(description);
        setInstant(instant);               
    }  
           
    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManagePunctualRoomsOccupationComment")
    public void edit(MultiLanguageString subject, MultiLanguageString description) {
	if(!getRequest().getCurrentState().equals(RequestState.NEW)) {
	    throw new DomainException("error.PunctualRoomsOccupationRequest.impossible.edit");
	}	
	setSubject(subject);
	setDescription(description);
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getInstant() != null && getSubject() != null && !getSubject().isEmpty()
		&& getDescription() != null && !getDescription().isEmpty();
    }
    
    public String getPresentationInstant() {
	return getInstant().toString("dd/MM/yyyy HH:mm");
    }
    
    public RequestState getState() {
	return getRequest().getState(getInstant());
    }
    
    @Override
    public void setInstant(DateTime instant) {
	if(instant == null) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.instant");
	}
        super.setInstant(instant);
    }
    
    @Override
    public void setDescription(MultiLanguageString description) {	
	if(description == null || description.isEmpty()) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.description");
	}
	super.setDescription(description);
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
	if(subject == null || subject.isEmpty()) {
	    throw new DomainException("error.PunctualRoomsOccupationComment.empty.subject");
	}
	super.setSubject(subject);
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
    
    private void checkIfCommentAlreadyExists(Person owner, MultiLanguageString subject, MultiLanguageString description) {
	List<PunctualRoomsOccupationComment> comments = owner.getPunctualRoomsOccupationComments();
	for (PunctualRoomsOccupationComment comment : comments) {	    
	    if(comment.getDescription().compareTo(description) == 0) {
		throw new DomainException("error.PunctualRoomsOccupationComment.comment.already.exists");
	    }
	}
    }
}
