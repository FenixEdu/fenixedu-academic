package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;

public class ResultEventAssociation extends ResultEventAssociation_Base {

    public enum ResultEventAssociationRole {
	Exhibitor, Participant;

	public String getName() {
	    return name();
	}

	public static ResultEventAssociationRole getDefaultRole(){
	    return Exhibitor;
	}
    }

    public ResultEventAssociation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResultEventAssociation(Result result, Event event, ResultEventAssociationRole role) {
	this();
	checkParameters(result, event, role);
	// This set is made first in order to make access control verifications on ResultPredicates.
	super.setResult(result);	
	setEditAll(result, event, role);
    }

    @Checked("ResultPredicates.eventWritePredicate")
    public void setEditAll(Result result, Event event, ResultEventAssociationRole role){
	super.setResult(result);
	super.setRole(role);
	super.setEvent(event);
    }
    
    public static ResultEventAssociation readByOid(Integer oid) {
	final ResultEventAssociation association = RootDomainObject.getInstance().readResultEventAssociationByOID(oid);
	
	if (association==null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.null");
	}
	
	return association;
    }

    /**
     * Setters block!
     */
    @Override
    public void setResult(Result Result) {
	throw new DomainException("error.researcher.ResultEventAssociation.call","setResult");
    }

    @Override
    public void setEvent(Event Event) {
	throw new DomainException("error.researcher.ResultEventAssociation.call","setEvent");
    }
    
    @Override
    public void setRole(ResultEventAssociationRole role) {
	throw new DomainException("error.researcher.ResultEventAssociation.call","setRole");    
    }
    
    @Override
    public void removeResult(){
	throw new DomainException("error.researcher.ResultEventAssociation.call","removeResult");
    }
    
    @Override
    public void removeEvent(){
	throw new DomainException("error.researcher.ResultEventAssociation.call","removeEvent");
    }
    
    private void checkParameters(Result result, Event event, ResultEventAssociationRole role) {
	if (result == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.result.null");
	}
	if (event == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.event.null");
	}
	if (role == null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.role.null");
	}
	if(result.hasAssociationWithEventRole(event, role)) {
	    throw new DomainException("error.researcher.ResultEventAssociation.association.exists");
	}
    }

    /**
     * Method responsible for deleting a ResultEventAssociation
     */
    @Checked("ResultPredicates.eventWritePredicate")
    public void delete() {
	final Event event = this.getEvent();

	removeReferences();
	removeRootDomainObject();
	deleteDomainObject();

	event.sweep();
    }
    
    private void removeReferences() {
	super.setResult(null);
	super.setEvent(null);
    }
}
