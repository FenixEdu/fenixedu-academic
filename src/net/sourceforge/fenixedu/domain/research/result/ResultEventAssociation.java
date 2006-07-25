package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;

public class ResultEventAssociation extends ResultEventAssociation_Base {
    
    public ResultEventAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultEventAssociation(Result result, Event event, ResultEventAssociationRole role, String personName) {
        this();
        checkParameters(result, event, role, personName);
        setResult(result);
        setRole(role);
        setEvent(event);
        setChangedBy(personName);
    }
    
    private void checkParameters(Result result, Event event, ResultEventAssociationRole role, String personName) {
        if (result == null) {
            throw new DomainException("error.ResultEventAssociation.result.cannot.be.null");
        }
        if (event == null) {
            throw new DomainException("error.ResultEventAssociation.event.cannot.be.null");
        }
        if (role == null) {
            throw new DomainException("error.ResultAssociations.role.cannot.be.null");
        }
        if (personName == null || personName.equals("")) {
            throw new DomainException("error.ResultAssociations.changedBy.null");
        }
        
        if(result.hasAssociationWithEventRole(event, role)) {
            throw new DomainException("error.ResultAssociations.association.exists");
        }
    }

    public void setChangedBy(String personName) {
        this.getResult().setModificationDateAndAuthor(personName);
    }
    
    public String getChangedBy(){
        return this.getResult().getModifyedBy();
    }
    public void delete() {
        this.removeResult();
        this.removeEvent();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public void delete(String personName){
        final Event event = this.getEvent();
        final Result result = this.getResult();
        
        this.delete();
        event.sweep();
        
        result.setModificationDateAndAuthor(personName);
    }
    
    public String getResultType() {
        return this.getResult().getClass().getSimpleName();        
    }
    
    public void setResultType(String resultType) {    }
    
    public enum ResultEventAssociationRole {
        Exhibitor,
        Participant;
        
        public String getName() {
            return name();
        }
        
        public static ResultEventAssociationRole getDefaultEventRoleType(){
            return Exhibitor;
        }
    }    
}
