package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

public class PunctualRoomsOccupationRequest extends PunctualRoomsOccupationRequest_Base {
    
    public static final Comparator<PunctualRoomsOccupationRequest> COMPARATOR_BY_INSTANT = new ComparatorChain();
    public static final Comparator<PunctualRoomsOccupationRequest> COMPARATOR_BY_IDENTIFICATION = new BeanComparator("identification");
    static {
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("instant"), true);	
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("idInternal"));
    }
    
    public PunctualRoomsOccupationRequest(Person requestor, MultiLanguageString subject, MultiLanguageString description) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRequestor(requestor);
        DateTime now = new DateTime();
        setInstant(now);
        addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.NEW, now));
        addComments(new PunctualRoomsOccupationComment(this, subject, description, requestor, now, null, null));      
        setIdentification(getNextRequestIdentification());
    }
    
    @Override
    public void setOwner(Person owner) {
	if(owner == null || !owner.equals(getRequestor())) {
	    super.setOwner(owner);   
	}	
    }
    
    @Override
    public void setIdentification(Integer identification) {
	if(identification == null) {
	    throw new DomainException("error.PunctualRoomsOccupationRequest.empty.identification");
	}
	super.setIdentification(identification);
    }
    
    @Override
    public void setRequestor(Person requestor) {
	if(requestor == null) {
	    throw new DomainException("error.PunctualRoomsOccupationRequest.empty.requestor");
	}
	super.setRequestor(requestor);
    }
    
    public void openRequest(Person person) {
	if(getCurrentState().equals(RequestState.NEW)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.OPEN, new DateTime()));
	    setOwner(person);
	}
    }
    
    public void reOpenRequest(Person person) {
	if(getCurrentState().equals(RequestState.RESOLVED)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.OPEN, new DateTime()));
	    setOwner(person);
	}
    }
    
    public void reOpenRequest(DateTime instant) {
	if(getCurrentState().equals(RequestState.RESOLVED)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.OPEN, instant));
	}
    }  
    
    public void closeRequest(Person person) {
	if(!getCurrentState().equals(RequestState.RESOLVED)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.RESOLVED, new DateTime()));
	    setOwner(person);
	}
    }
    
    public void closeRequest(DateTime instant) {
	if(!getCurrentState().equals(RequestState.RESOLVED)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.RESOLVED, instant));
	}
    }  
           
    public String getPresentationInstant() {
	return getInstant().toString("dd/MM/yyyy HH:mm");
    }
          
    public static Set<PunctualRoomsOccupationRequest> getRequestsByTypeOrderByDate(RequestState state){
	Set<PunctualRoomsOccupationRequest> result = new TreeSet<PunctualRoomsOccupationRequest>(PunctualRoomsOccupationRequest.COMPARATOR_BY_INSTANT);
	for (PunctualRoomsOccupationRequest request : RootDomainObject.getInstance().getPunctualRoomsOccupationRequestsSet()) {
	    if(request.getCurrentState().equals(state)) {
		result.add(request);		
	    }
	}
	return result;
    }
    
    public static Set<PunctualRoomsOccupationRequest> getRequestsByTypeAndDiferentOwnerOrderByDate(RequestState state, Person owner){
	Set<PunctualRoomsOccupationRequest> result = new TreeSet<PunctualRoomsOccupationRequest>(PunctualRoomsOccupationRequest.COMPARATOR_BY_INSTANT);
	for (PunctualRoomsOccupationRequest request : RootDomainObject.getInstance().getPunctualRoomsOccupationRequestsSet()) {
	    if(request.getCurrentState().equals(state) && 
		    (request.getOwner() == null || !request.getOwner().equals(owner))) {
		result.add(request);		
	    }
	}
	return result;	
    }
           
    public PunctualRoomsOccupationComment getFirstComment() {
	for (PunctualRoomsOccupationComment comment : getComments()) {
	    if(comment.getInstant().isEqual(getInstant())) {
		return comment;
	    }
	}
	return null;
    }      
    
    public Set<PunctualRoomsOccupationComment> getCommentsWithoutFirstCommentOrderByDate(){
	Set<PunctualRoomsOccupationComment> result = new TreeSet<PunctualRoomsOccupationComment>(PunctualRoomsOccupationComment.COMPARATOR_BY_INSTANT);	
	for (PunctualRoomsOccupationComment comment : getComments()) {
	    if(!comment.getInstant().isEqual(getInstant())) {
		result.add(comment);
	    }
	}
	return result;
    }
    
    public String getSubject() {
	PunctualRoomsOccupationComment firstComment = getFirstComment();
	return firstComment != null ? firstComment.getSubject().getContent() : null;
    }
    
    public String getDescription() {
	PunctualRoomsOccupationComment firstComment = getFirstComment();
	return firstComment != null ? firstComment.getDescription().getContent() : null;
    }
         
    public RequestState getCurrentState() {
	SortedSet<PunctualRoomsOccupationStateInstant> result =
	    new TreeSet<PunctualRoomsOccupationStateInstant>(PunctualRoomsOccupationStateInstant.COMPARATOR_BY_INSTANT);
	
	result.addAll(getStateInstants());
	return result.last().getRequestState();
    }

    public RequestState getState(DateTime instanTime) {
	if(instanTime == null) {
	    return getCurrentState();
	} else {
	    for (PunctualRoomsOccupationStateInstant stateInstant : getStateInstants()) {
		if(stateInstant.getInstant().isEqual(instanTime)) {
		    return stateInstant.getRequestState();
		}
	    }
	}		
	return null;
    }  
    
    private Integer getNextRequestIdentification() {
	SortedSet<PunctualRoomsOccupationRequest> result = new TreeSet<PunctualRoomsOccupationRequest>(PunctualRoomsOccupationRequest.COMPARATOR_BY_IDENTIFICATION);
	List<PunctualRoomsOccupationRequest> requests = RootDomainObject.getInstance().getPunctualRoomsOccupationRequests();
	for (PunctualRoomsOccupationRequest request : requests) {
	    if(!request.equals(this)) {
		result.add(request);	
	    }
	}	
	return result.isEmpty() ? 1 : result.last().getIdentification() + 1;
    }
}
