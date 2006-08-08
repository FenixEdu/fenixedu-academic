package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ResultUnitAssociation extends ResultUnitAssociation_Base {
    
    public  ResultUnitAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultUnitAssociation(Result result, Unit unit, ResultUnitAssociationRole role, String personName) {
        this();
        checkParameters(result, unit, role, personName);
        setResult(result);
        setUnit(unit);
        setRole(role);
        setChangedBy(personName);
    }
    
    private void checkParameters(Result result, Unit unit, ResultUnitAssociationRole role, String personName) {
        if (result == null) {
            throw new DomainException("error.ResultUnitAssociation.result.cannot.be.null");
        }
        if (unit == null) {
            throw new DomainException("error.ResultEventAssociation.unit.cannot.be.null");
        }
        if (role == null) {
            throw new DomainException("error.ResultAssociations.role.cannot.be.null");
        }
        if (personName == null || personName.equals("")) {
            throw new DomainException("error.Result.person.not.found");
        }
        
        if(result.hasAssociationWithUnitRole(unit,role)){
            throw new DomainException("error.ResultAssociations.association.exists");
        }
    }

    public void setChangedBy(String personName) {
        if (personName == null ) { throw new DomainException("error.ResultAssociations.changedBy.null"); }
        this.getResult().setModificationDateAndAuthor(personName);
    }
    
    public String getChangedBy() {
        return this.getResult().getModifyedBy();
    }
    
    public void delete() {
        this.removeResult();
        this.removeUnit();
        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public void delete(String personName){        
        final Result result = this.getResult();
        
        this.delete();
        
        result.setModificationDateAndAuthor(personName);
    }
    
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
}
