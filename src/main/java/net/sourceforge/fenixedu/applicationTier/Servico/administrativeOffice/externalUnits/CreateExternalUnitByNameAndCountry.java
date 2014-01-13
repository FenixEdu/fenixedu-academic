package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class CreateExternalUnitByNameAndCountry {

    @Atomic
    public static Unit run(String name, Country country) {
        return Unit.createNewNoOfficialExternalInstitution(name, country);
    }
}