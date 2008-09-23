package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalUnitByName extends FenixService {

    public Unit run(String name) {
	return Unit.createNewNoOfficialExternalInstitution(name);
    }
}
