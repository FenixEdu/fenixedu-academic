package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;


import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExternalUnitByName {

    @Service
    public static Unit run(String name) {
        return Unit.createNewNoOfficialExternalInstitution(name);
    }
}