package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllInstitutions {

    @Atomic
    public static Object run() throws FenixServiceException {
        return UnitUtils.readAllExternalInstitutionUnits();
    }
}