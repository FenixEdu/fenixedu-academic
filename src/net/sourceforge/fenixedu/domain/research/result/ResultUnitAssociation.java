package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ResultUnitAssociation extends ResultUnitAssociation_Base {

    public enum ResultUnitAssociationRole {
	Sponsor, Participant;

	public String getName() {
	    return name();
	}

	public static ResultUnitAssociationRole getDefaultRole() {
	    return Sponsor;
	}
    }

    public ResultUnitAssociation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResultUnitAssociation(Result result, Unit unit, ResultUnitAssociationRole role) {
	this();
	checkParameters(result, unit, role);
	// This set is made first in order to make access control verifications on ResultPredicates.
	super.setResult(result);
	setEditAll(result, unit, role);
    }

    @Checked("ResultPredicates.unitWritePredicate")
    public void setEditAll(Result result, Unit unit, ResultUnitAssociationRole role) {
	super.setResult(result);
	super.setUnit(unit);
	super.setRole(role);
    }
    
    public static ResultUnitAssociation readByOid(Integer oid) {
	final ResultUnitAssociation association = RootDomainObject.getInstance().readResultUnitAssociationByOID(oid);
	
	if (association==null) {
	    throw new DomainException("error.researcher.ResultUnitAssociation.null");
	}
	
	return association;
    }

    /**
     * Setters block!
     */
    @Override
    public void setResult(Result Result) {
	throw new DomainException("error.researcher.ResultUnitAssociation.call","setResult");
    }

    @Override
    public void setUnit(Unit Unit) {
	throw new DomainException("error.researcher.ResultUnitAssociation.call","setUnit");
    }

    @Override
    public void setRole(ResultUnitAssociationRole role) {
	throw new DomainException("error.researcher.ResultUnitAssociation.call","setRole");
    }

    @Override
    public void removeUnit() {
	throw new DomainException("error.researcher.ResultUnitAssociation.call","removeUnit");
    }

    @Override
    public void removeResult() {
	throw new DomainException("error.researcher.ResultUnitAssociation.call","removeResult");
    }

    /**
     * Method responsible for deleting a ResultUnitAssociation
     */
    @Checked("ResultPredicates.unitWritePredicate")
    public void delete() {
	removeReferences();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void removeReferences() {
	super.setResult(null);
	super.setUnit(null);
    }

    private void checkParameters(Result result, Unit unit, ResultUnitAssociationRole role) {
	if (result == null) {
	    throw new DomainException("error.researcher.ResultUnitAssociation.result.null");
	}
	if (unit == null) {
	    throw new DomainException("error.researcher.ResultUnitAssociation.unit.null");
	}
	if (role == null) {
	    throw new DomainException("error.researcher.ResultUnitAssociation.role.null");
	}
	if (result.hasAssociationWithUnitRole(unit, role)) {
	    throw new DomainException("error.researcher.ResultUnitAssociation.association.exists");
	}
    }
}
