package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadExternalPersonByID {

    @Service
    public static Object run(String externalPersonID) throws FenixServiceException {
        InfoExternalPerson infoExternalPerson = null;
        ExternalContract externalPerson = null;

        externalPerson = (ExternalContract) FenixFramework.getDomainObject(externalPersonID);
        if (externalPerson == null) {
            throw new NonExistingServiceException("error.exception.commons.ExternalPersonNotFound");
        }

        infoExternalPerson = InfoExternalPerson.newInfoFromDomain(externalPerson);

        return infoExternalPerson;
    }

}