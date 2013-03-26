package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExternalUnitByNameAndCountry extends FenixService {

    @Service
    public static Unit run(String name, Country country) {
        return Unit.createNewNoOfficialExternalInstitution(name, country);
    }
}