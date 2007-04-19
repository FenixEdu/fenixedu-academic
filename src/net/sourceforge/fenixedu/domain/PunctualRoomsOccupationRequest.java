package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

public class PunctualRoomsOccupationRequest extends PunctualRoomsOccupationRequest_Base {
        
    public static final Comparator<PunctualRoomsOccupationRequest> COMPARATOR_BY_IDENTIFICATION = new BeanComparator("identification");
    public static final Comparator<PunctualRoomsOccupationRequest> COMPARATOR_BY_INSTANT = new ComparatorChain();
    public static final Comparator<PunctualRoomsOccupationRequest> COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT = new ComparatorChain();
    static {	
	((ComparatorChain) COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT).addComparator(new BeanComparator("moreRecentCommentInstant"), true);
	((ComparatorChain) COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT).addComparator(DomainObject.COMPARATOR_BY_ID);
	
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("instant"), true);	
	((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    public PunctualRoomsOccupationRequest(Person requestor, MultiLanguageString subject, MultiLanguageString description) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRequestor(requestor);        
        DateTime now = new DateTime();
        setInstant(now);
        addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.NEW, now));
        addComments(new PunctualRoomsOccupationComment(this, subject, description, requestor, now));
        setTeacherReadComments(1);
        setEmployeeReadComments(0);
        setIdentification(getNextRequestIdentification());        
    }
          
    public Integer getNumberOfNewComments(Person person) {		
	if(person.equals(getOwner())) {
	    return getCommentsCount() - getEmployeeReadComments();	    
	} else if(person.equals(getRequestor())) {
	    return getCommentsCount() - getTeacherReadComments();
	}	
	return Integer.valueOf(0);
    }
              
    public DateTime getMoreRecentCommentInstant() {
	SortedSet<PunctualRoomsOccupationComment> result = new TreeSet<PunctualRoomsOccupationComment>(PunctualRoomsOccupationComment.COMPARATOR_BY_INSTANT);
	result.addAll(getComments());
	return result.last().getInstant();
    }
    
    public void associateNewGenericEvent(Person person, GenericEvent event, DateTime instant) {
	if (getCurrentState().equals(RequestState.RESOLVED)) {
	    throw new DomainException("error.PunctualRoomsOccupationRequest.impossible.associate.new.genericEvent");
	}
	addGenericEvents(event);
	openRequestWithoutAssociateOwner(instant);
	setOwner(person);
    }
    
    public void createNewTeacherOrEmployeeComment(MultiLanguageString description, Person commentOwner, DateTime instant) {	
	new PunctualRoomsOccupationComment(this, null, description, commentOwner, instant);			
	if(commentOwner.equals(getRequestor())) {
	    setTeacherReadComments(getCommentsCount());	    
	} else {
	    setOwner(commentOwner);
	    setEmployeeReadComments(getCommentsCount());	    
	}	
    }
    
    public void createNewTeacherCommentAndOpenRequest(MultiLanguageString description, Person commentOwner, DateTime instant) {
	new PunctualRoomsOccupationComment(this, null, description, commentOwner, instant);	
	openRequestWithoutAssociateOwner(instant);	
	setTeacherReadComments(getCommentsCount());	
    }
    
    public void createNewEmployeeCommentAndCloseRequest(MultiLanguageString description, Person commentOwner, DateTime instant) {
	new PunctualRoomsOccupationComment(this, null, description, commentOwner, instant);	
	closeRequestWithoutAssociateOwner(instant);
	setOwner(commentOwner);
	setEmployeeReadComments(getCommentsCount());	
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
       
    private void closeRequestWithoutAssociateOwner(DateTime instant) {
	if(!getCurrentState().equals(RequestState.RESOLVED)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.RESOLVED, instant));
	}
    }  

    private void openRequestWithoutAssociateOwner(DateTime instant) {
	if(!getCurrentState().equals(RequestState.OPEN)) {
	    addStateInstants(new PunctualRoomsOccupationStateInstant(this, RequestState.OPEN, instant));
	}
    }  
    
    public void closeRequestAndAssociateOwnerOnlyForEmployees(DateTime instant, Person person) {
	closeRequestWithoutAssociateOwner(instant);	
	if(!getOwner().equals(person)) {	    
	    setEmployeeReadComments(0);
	    setOwner(person);
	}		
    }  

    public void openRequestAndAssociateOwnerOnlyForEmployess(DateTime instant, Person person) {
	openRequestWithoutAssociateOwner(instant);	
	if(getOwner() == null || !getOwner().equals(person)) {
	    setEmployeeReadComments(0);
	    setOwner(person);
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
    
    public static Set<PunctualRoomsOccupationRequest> getResolvedRequestsOrderByMoreRecentComment(){
	Set<PunctualRoomsOccupationRequest> result = new TreeSet<PunctualRoomsOccupationRequest>(PunctualRoomsOccupationRequest.COMPARATOR_BY_MORE_RECENT_COMMENT_INSTANT);
	for (PunctualRoomsOccupationRequest request : RootDomainObject.getInstance().getPunctualRoomsOccupationRequestsSet()) {
	    if(request.getCurrentState().equals(RequestState.RESOLVED)) {
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
    
    public Set<InfoGenericEvent> getActiveGenericEvents(){
	Set<InfoGenericEvent> result = new HashSet<InfoGenericEvent>();
	for (GenericEvent genericEvent : getGenericEvents()) {
	    if(genericEvent.isActive()) {
		result.add(new InfoGenericEvent(genericEvent));
	    }
	}
	return result;
    }
}
