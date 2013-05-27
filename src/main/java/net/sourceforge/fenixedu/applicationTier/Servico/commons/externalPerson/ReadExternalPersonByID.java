package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadExternalPersonByID {

    @Service
    public static Object run(Integer externalPersonID) throws FenixServiceException {
        InfoExternalPerson infoExternalPerson = null;
        ExternalContract externalPerson = null;

        externalPerson = (ExternalContract) RootDomainObject.getInstance().readAccountabilityByOID(externalPersonID);
        if (externalPerson == null) {
            throw new NonExistingServiceException("error.exception.commons.ExternalPersonNotFound");
        }

        infoExternalPerson = InfoExternalPerson.newInfoFromDomain(externalPerson);

        return infoExternalPerson;
    }

}