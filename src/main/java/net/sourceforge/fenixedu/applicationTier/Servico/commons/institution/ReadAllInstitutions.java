package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllInstitutions {

    @Service
    public static Object run() throws FenixServiceException {
        return UnitUtils.readAllExternalInstitutionUnits();
    }
}