package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class CreateExternalUnitByName {

    @Atomic
    public static Unit run(String name) {
        return Unit.createNewNoOfficialExternalInstitution(name);
    }
}