package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class EditExternalPerson implements IService {

    /**
     * The actor of this class.
     */
    public EditExternalPerson() {
    }

    public void run(Integer externalPersonID, String name, String address, Integer workLocationID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException {
        IExternalPerson storedExternalPerson = null;
        IExternalPerson existingExternalPerson = null;

        IWorkLocation storedWorkLocation = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            storedExternalPerson = (IExternalPerson) sp.getIPersistentExternalPerson().readByOID(
                    ExternalPerson.class, externalPersonID);

            if (storedExternalPerson == null)
                throw new NonExistingServiceException(
                        "error.exception.externalPerson.nonExistingExternalPsrson");

            existingExternalPerson = sp.getIPersistentExternalPerson()
                    .readByNameAndAddressAndWorkLocationID(name, address, workLocationID);

            // checks if existes another exernal person with the same name,
            // address and name location
            if (existingExternalPerson != null) {
                if (!storedExternalPerson.getIdInternal().equals(existingExternalPerson.getIdInternal()))
                    throw new ExistingServiceException(
                            "error.exception.externalPerson.existingExternalPerson");
            }

            sp.getIPersistentExternalPerson().simpleLockWrite(storedExternalPerson);

            storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(
                    WorkLocation.class, workLocationID);
            storedExternalPerson.setWorkLocation(storedWorkLocation);

            IPerson person = storedExternalPerson.getPerson();
            sp.getIPessoaPersistente().lockWrite(person);

            person.setNome(name);
            person.setMorada(address);
            person.setTelefone(phone);
            person.setTelemovel(mobile);
            person.setEnderecoWeb(homepage);
            person.setEmail(email);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }
}