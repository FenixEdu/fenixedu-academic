package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixWebFramework.services.Service;

public class ReadInstitutionByName {

    @Service
    public static Unit run(String institutionName) throws FenixServiceException {
        return UnitUtils.readExternalInstitutionUnitByName(institutionName);
    }
}