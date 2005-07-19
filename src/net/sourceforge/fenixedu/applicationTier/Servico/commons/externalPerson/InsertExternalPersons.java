package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.workLocation.InsertWorkLocation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertExternalPersons implements IService {

    public List<IExternalPerson> run(List<InfoExternalPerson> infoExternalPersons) throws ExcepcaoPersistencia, ExistingServiceException {

        List<IExternalPerson> externalPersons = new ArrayList();


            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List<IWorkLocation> workLocations = sp.getIPersistentWorkLocation().readAll();

            // generate new identification number
            String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
            int nextID = Integer.parseInt(lastDocumentIdNumber);

            for (InfoExternalPerson infoExternalPerson : infoExternalPersons) {

            IWorkLocation currentWorkLocation = null;

            // retrieving existing work location
            for (IWorkLocation workLocation : workLocations) {
                if (workLocation.getName()
                        .equals(infoExternalPerson.getInfoWorkLocation().getName())) {
                    currentWorkLocation = workLocation;
                    break;
                }
            }

            // creating a new one if it doesn't already exist
            if (currentWorkLocation == null) {
                InsertWorkLocation iwl = new InsertWorkLocation();
                currentWorkLocation = iwl.run(infoExternalPerson.getInfoWorkLocation().getName());
                workLocations.add(currentWorkLocation);
            }

            // creating a new Person to relate to
            IPerson person = new Person();
            person.setNome(infoExternalPerson.getInfoPerson().getNome());
            person.setGender(Gender.MALE);
            person.setMorada("");
            person.setTelefone("");
            person.setTelemovel("");
            person.setEnderecoWeb("");
            person.setEmail("");
            nextID++;
            person.setNumeroDocumentoIdentificacao("" + nextID);
            person.setIdDocumentType(IDDocumentType.EXTERNAL);
            person.setUsername("e" + nextID);

            sp.getIPessoaPersistente().simpleLockWrite(person);

            // creating a new ExternalPerson
            IExternalPerson externalPerson = new ExternalPerson();
            externalPerson.setPerson(person);
            externalPerson.setWorkLocation(currentWorkLocation);

            sp.getIPersistentExternalPerson().simpleLockWrite(externalPerson);

            externalPersons.add(externalPerson);
        }


        return externalPersons;
    }
}