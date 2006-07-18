/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class DisassociateParentUnit extends Service {

    public void run(Integer unitID, Integer parentUnitID) throws FenixServiceException {
        Unit parentUnit = getParentUnit(parentUnitID);
        Unit unit = (Unit) rootDomainObject.readPartyByOID(unitID);

        if (unit == null) {
            throw new FenixServiceException("error.inexistent.unit");
        }
        unit.removeParentUnit(parentUnit);
    }

    private Unit getParentUnit(Integer parentUnitID) {
        Unit parentUnit = null;
        if (parentUnitID != null) {
            parentUnit = (Unit) rootDomainObject.readPartyByOID(parentUnitID);
        }
        return parentUnit;
    }
}
