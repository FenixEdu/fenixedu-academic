package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.FenixFramework;

public class ResultUnitAssociation extends ResultUnitAssociation_Base {

    public enum ResultUnitAssociationRole {
        Sponsor, Participant;

        public static ResultUnitAssociationRole getDefaultRole() {
            return Participant;
        }
    }

    private ResultUnitAssociation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    ResultUnitAssociation(ResearchResult result, Unit unit, ResultUnitAssociationRole role) {
        this();
        checkParameters(result, unit, role);
        fillAllAttributes(result, unit, role);
    }

    @Override
    public void setRole(ResultUnitAssociationRole role) {
        check(this, ResultPredicates.unitWritePredicate);
        if (role == null) {
            throw new DomainException("error.researcher.ResultUnitAssociation.role.null");
        }
        if (!this.getRole().equals(role)) {
            if (!this.getResult().hasAssociationWithUnitRole(this.getUnit(), role)) {
                super.setRole(role);
                this.getResult().setModifiedByAndDate();
            } else {
                throw new DomainException("error.researcher.ResultUnitAssociation.association.exists", this.getUnit().getName(),
                        this.getRole().toString());
            }
        }
    }

    public final static ResultUnitAssociation readByOid(String oid) {
        final ResultUnitAssociation association = FenixFramework.getDomainObject(oid);

        if (association == null) {
            throw new DomainException("error.researcher.ResultUnitAssociation.null");
        }

        return association;
    }

    /**
     * Method responsible for deleting a ResultUnitAssociation
     */
    public final void delete() {
        removeReferences();
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void removeReferences() {
        super.setResult(null);
        super.setUnit(null);
    }

    private void fillAllAttributes(ResearchResult result, Unit unit, ResultUnitAssociationRole role) {
        super.setResult(result);
        super.setUnit(unit);
        super.setRole(role);
    }

    private void checkParameters(ResearchResult result, Unit unit, ResultUnitAssociationRole role) {
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

    /**
     * Setters block!
     */
    @Override
    public void setResult(ResearchResult Result) {
        throw new DomainException("error.researcher.ResultUnitAssociation.call", "setResult");
    }

    @Override
    public void setUnit(Unit Unit) {
        throw new DomainException("error.researcher.ResultUnitAssociation.call", "setUnit");
    }

    @Deprecated
    public boolean hasResult() {
        return getResult() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRole() {
        return getRole() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
