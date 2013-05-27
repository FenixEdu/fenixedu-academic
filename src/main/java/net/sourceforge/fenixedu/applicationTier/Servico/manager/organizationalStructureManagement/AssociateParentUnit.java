package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AssociateParentUnit {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer unitID, Integer parentUnitID, AccountabilityType accountabilityType)
            throws FenixServiceException {

        Unit parentUnit = getParentUnit(parentUnitID);
        Unit unit = (Unit) RootDomainObject.getInstance().readPartyByOID(unitID);

        if (unit == null) {
            throw new FenixServiceException("error.inexistent.unit");
        }
        unit.addParentUnit(parentUnit, accountabilityType);
    }

    private static Unit getParentUnit(Integer parentUnitID) {
        Unit parentUnit = null;
        if (parentUnitID != null) {
            parentUnit = (Unit) RootDomainObject.getInstance().readPartyByOID(parentUnitID);
        }
        return parentUnit;
    }
}