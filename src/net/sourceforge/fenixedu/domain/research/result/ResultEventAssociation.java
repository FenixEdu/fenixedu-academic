package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

public class ResultEventAssociation extends ResultEventAssociation_Base {
    
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
    
    public ResultEventAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultEventAssociation(Result result, Event event, ResultEventAssociationRole role) {
        this();
        checkParameters(result, event, role);
        setResult(result);
        setRole(role);
        setEvent(event);
        setChangedBy();
    }
    
    private void checkParameters(Result result, Event event, ResultEventAssociationRole role) {
        if (result == null) {
            throw new DomainException("error.ResultEventAssociation.result.cannot.be.null");
        }
        if (event == null) {
            throw new DomainException("error.ResultEventAssociation.event.cannot.be.null");
        }
        if (role == null) {
            throw new DomainException("error.ResultAssociations.role.cannot.be.null");
        }
        if(result.hasAssociationWithEventRole(event, role)) {
            throw new DomainException("error.ResultAssociations.association.exists");
        }
    }

    public void setChangedBy() {
        this.getResult().setModificationDateAndAuthor();
    }
    
    public void delete() {
    	final Event event = this.getEvent();
    	
        this.removeResult();
        this.removeEvent();
        this.removeRootDomainObject();
        deleteDomainObject();
        
        event.sweep();
    }

    /**
     * Method used to call the service responsible for creating a ResultEventAssociation
     * 
     * @param bean
     * @throws FenixFilterException
     * @throws FenixServiceException
     */
	public static void create(ResultEventAssociationCreationBean bean) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "CreateResultEventAssociation", bean);   
	}

	/**
	 * Method used to call the service responsible for removing a ResultEventAssociation
	 * 
	 * @param associationId
	 * @throws FenixFilterException
	 * @throws FenixServiceException
	 */
	public static void remove(Integer associationId) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "DeleteResultEventAssociation", associationId);
	}    
}
