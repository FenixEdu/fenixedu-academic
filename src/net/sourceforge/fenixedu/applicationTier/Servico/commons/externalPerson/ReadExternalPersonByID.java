package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadExternalPersonByID implements IService {

    /**
     * The actor of this class.
     */
    public ReadExternalPersonByID() {
    }

    public Object run(Integer externalPersonID) throws FenixServiceException {
        InfoExternalPerson infoExternalPerson = null;
        IExternalPerson externalPerson = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            externalPerson = (IExternalPerson) sp.getIPersistentExternalPerson().readByOID(
                    ExternalPerson.class, externalPersonID);

            if (externalPerson == null)
                throw new NonExistingServiceException("error.exception.commons.ExternalPersonNotFound");

            infoExternalPerson = Cloner.copyIExternalPerson2InfoExternalPerson(externalPerson);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoExternalPerson;
    }
}