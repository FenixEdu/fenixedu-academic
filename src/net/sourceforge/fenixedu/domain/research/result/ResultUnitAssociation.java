package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

public class ResultUnitAssociation extends ResultUnitAssociation_Base {
    
    public enum ResultUnitAssociationRole {
        Sponsor,
        Participant;
        
        public String getName() {
            return name();
        }
        
        public static ResultUnitAssociationRole getDefaultUnitRoleType(){
            return Sponsor;
        }
    }
    
	public  ResultUnitAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultUnitAssociation(Result result, Unit unit, ResultUnitAssociationRole role) {
        this();
        checkParameters(result, unit, role);
        setResult(result);
        setUnit(unit);
        setRole(role);
        setChangedBy();
    }
    
    private void checkParameters(Result result, Unit unit, ResultUnitAssociationRole role) {
        if (result == null) {
            throw new DomainException("error.ResultUnitAssociation.result.cannot.be.null");
        }
        if (unit == null) {
            throw new DomainException("error.ResultEventAssociation.unit.cannot.be.null");
        }
        if (role == null) {
            throw new DomainException("error.ResultAssociations.role.cannot.be.null");
        }
        if(result.hasAssociationWithUnitRole(unit,role)){
            throw new DomainException("error.ResultAssociations.association.exists");
        }
    }

    public void setChangedBy() {
        this.getResult().setModificationDateAndAuthor();
    }
    
    public void delete() {
        this.removeResult();
        this.removeUnit();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    /**
     * Method used to call the service responsible for creating a ResultUnitAssociation
     *  
     * @param bean
     * @throws FenixFilterException
     * @throws FenixServiceException
     */
	public static void create(ResultUnitAssociationCreationBean bean) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "CreateResultUnitAssociation", bean);
	}

	/**
	 * Method used to call the service responsible for removing a ResultUnitAssociation
	 * 
	 * @param associationId
	 * @throws FenixFilterException
	 * @throws FenixServiceException
	 */
	public static void remove(Integer associationId) throws FenixFilterException, FenixServiceException {
		ServiceUtils.executeService(AccessControl.getUserView(), "DeleteResultUnitAssociation", associationId);
	}
}
