package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoDocumentoIdentificacao;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class InsertExternalPerson implements IService {

    /**
     * The actor of this class.
     */
    public InsertExternalPerson() {
    }

    public void run(String name, String address, Integer workLocationID, String phone, String mobile,
            String homepage, String email) throws FenixServiceException {
        IExternalPerson externalPerson = null;
        IExternalPerson storedExternalPerson = null;
        IPerson person = null;
        IWorkLocation storedWorkLocation = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            storedExternalPerson = sp.getIPersistentExternalPerson()
                    .readByNameAndAddressAndWorkLocationID(name, address, workLocationID);

            if (storedExternalPerson != null)
                throw new ExistingServiceException(
                        "error.exception.commons.externalPerson.existingExternalPerson");

            storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(
                    WorkLocation.class, workLocationID);

            //generate new identification number
            String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
            int nextID = Integer.parseInt(lastDocumentIdNumber) + 1;
            lastDocumentIdNumber = "" + nextID;

            externalPerson = new ExternalPerson();
            person = new Person();

            person.setNome(name);
            person.setMorada(address);
            person.setTelefone(phone);
            person.setTelemovel(mobile);
            person.setEnderecoWeb(homepage);
            person.setEmail(email);
            person.setNumeroDocumentoIdentificacao(lastDocumentIdNumber);
            person.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
                    TipoDocumentoIdentificacao.EXTERNO));
            person.setUsername("e" + lastDocumentIdNumber);

            sp.getIPessoaPersistente().simpleLockWrite(person);

            externalPerson.setPerson(person);
            externalPerson.setWorkLocation(storedWorkLocation);

            sp.getIPersistentExternalPerson().simpleLockWrite(externalPerson);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }
}