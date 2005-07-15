package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
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
public class InsertExternalPerson implements IService {

    public IExternalPerson run(String name, String sex, String address, Integer workLocationID, String phone,
            String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IExternalPerson storedExternalPerson = sp.getIPersistentExternalPerson()
                .readByNameAndAddressAndWorkLocationID(name, address, workLocationID);

        if (storedExternalPerson != null)
            throw new ExistingServiceException(
                    "error.exception.commons.externalPerson.existingExternalPerson");

        IWorkLocation storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(
                WorkLocation.class, workLocationID);

        // generate new identification number
        String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
        int nextID = Integer.parseInt(lastDocumentIdNumber) + 1;
        lastDocumentIdNumber = "" + nextID;

        IPerson person = new Person();
        person.setNome(name);
        person.setGender(Gender.valueOf(sex));
        person.setMorada(address);
        person.setTelefone(phone);
        person.setTelemovel(mobile);
        person.setEnderecoWeb(homepage);
        person.setEmail(email);
        person.setNumeroDocumentoIdentificacao(lastDocumentIdNumber);
        person.setIdDocumentType(IDDocumentType.EXTERNAL);
        person.setUsername("e" + lastDocumentIdNumber);

        sp.getIPessoaPersistente().simpleLockWrite(person);

        IExternalPerson externalPerson = new ExternalPerson();        
        externalPerson.setPerson(person);
        if (storedWorkLocation != null) {
            externalPerson.setWorkLocation(storedWorkLocation);
        }

        sp.getIPersistentExternalPerson().simpleLockWrite(externalPerson);
        
        return externalPerson;

    }
}