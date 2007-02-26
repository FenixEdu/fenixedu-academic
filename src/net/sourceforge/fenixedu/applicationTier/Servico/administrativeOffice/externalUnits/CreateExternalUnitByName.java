package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalUnitByName extends Service {
	
	public Unit run(String name) {
		return Unit.createNewExternalInstitution(name);
	}
}
