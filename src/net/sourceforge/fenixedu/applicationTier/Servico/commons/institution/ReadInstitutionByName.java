package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadInstitutionByName extends Service {

    public Unit run(String institutionName) throws FenixServiceException{        
        return UnitUtils.readExternalInstitutionUnitByName(institutionName);
    }
}