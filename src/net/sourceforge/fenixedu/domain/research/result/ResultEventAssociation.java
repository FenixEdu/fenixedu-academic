package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class ResultEventAssociation extends ResultEventAssociation_Base {

    public enum ResultEventAssociationRole {
	Exhibitor, Participant;

	public static ResultEventAssociationRole getDefaultRole(){
	    return Exhibitor;
	}
    }

    private ResultEventAssociation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    ResultEventAssociation(ResearchResult result, EventEdition event, ResultEventAssociationRole role) {
	this();
	checkParameters(result, event, role);
	fillAllAttributes(result, event, role);
    }
    
    @Override
    @Checked("ResultPredicates.eventWritePredicate")
    public void setRole(ResultEventAssociationRole role){
	if (role==null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.role.null");
	}
	if(!this.getRole().equals(role)) {
	    if (!this.getResult().hasAssociationWithEventRole(this.getEvent(), role)) {
		super.setRole(role);
		this.getResult().setModifiedByAndDate();
	    } else {
		throw new DomainException("error.researcher.ResultEventAssociation.association.exists",
			this.getEvent().getFullName(), this.getRole().toString());
	    }
	}
    }
    
    public final static ResultEventAssociation readByOid(Integer oid) {
	final ResultEventAssociation association = RootDomainObject.getInstance().readResultEventAssociationByOID(oid);
	
	if (association==null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.null");
	}
	
	return association;
    }
    
    /**
     * Method responsible for deleting a ResultEventAssociation
     */
    public final void delete() {
	final EventEdition event = this.getEvent();

	removeReferences();
	removeRootDomainObject();
	deleteDomainObject();

	event.sweep();
    }
    
    private void fillAllAttributes(ResearchResult result, EventEdition event, ResultEventAssociationRole role){
	super.setResult(result);
	super.setEvent(event);
	super.setRole(role);
    }
    
    private void checkParameters(ResearchResult result, EventEdition event, ResultEventAssociationRole role) {
	if (result == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.result.null");
	}
	if (event == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.event.null");
	}
	if (role == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.role.null");
	}
	if (result.hasAssociationWithEventRole(event, role)) {
	    throw new DomainException("error.researcher.ResultEventAssociation.association.exists");
	}
    }
    
    private void removeReferences() {
	super.setResult(null);
	super.setEvent(null);
    }

    /**
     * Setters block!
     */
    @Override
    public void setResult(ResearchResult Result) {
	throw new DomainException("error.researcher.ResultEventAssociation.call","setResult");
    }

    @Override
    public void setEvent(EventEdition Event) {
	throw new DomainException("error.researcher.ResultEventAssociation.call","setEvent");
    }
    
    @Override
    public void removeResult(){
	throw new DomainException("error.researcher.ResultEventAssociation.call","removeResult");
    }
    
    @Override
    public void removeEvent(){
	throw new DomainException("error.researcher.ResultEventAssociation.call","removeEvent");
    }
}
