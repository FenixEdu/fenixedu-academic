package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class MergeUnits extends Service {

    public void run(Unit fromUnit, Unit destinationUnit) {
	if(fromUnit != null && destinationUnit != null) {
	    Unit.mergeExternalUnits(fromUnit, destinationUnit);
	}
    }
}
