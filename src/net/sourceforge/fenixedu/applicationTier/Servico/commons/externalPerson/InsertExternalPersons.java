package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertExternalPersons implements IService {

    public List<IExternalPerson> run(List<InfoExternalPerson> infoExternalPersons)
            throws ExcepcaoPersistencia, ExistingServiceException {

        List<IExternalPerson> externalPersons = new ArrayList<IExternalPerson>();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<IInstitution> institutions = (List<IInstitution>)sp.getIPersistentInstitution().readAll();

        // generate new identification number
        String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
        int nextID = Integer.parseInt(lastDocumentIdNumber);

        for (InfoExternalPerson infoExternalPerson : infoExternalPersons) {

            IInstitution currentInstitution = null;

            // retrieving existing work location
            for (IInstitution institution : institutions) {
                if (institution.getName().equals(infoExternalPerson.getInfoInstitution().getName())) {
                    currentInstitution = institution;
                    break;
                }
            }

            // creating a new one if it doesn't already exist
            if (currentInstitution == null) {
                InsertInstitution iwl = new InsertInstitution();
                currentInstitution = iwl.run(infoExternalPerson.getInfoInstitution().getName());
                institutions.add(currentInstitution);
            }

            String name = infoExternalPerson.getInfoPerson().getNome();
            String documentIDNumber = String.valueOf(++nextID);

            // creating a new ExternalPerson
            IExternalPerson externalPerson = DomainFactory.makeExternalPerson(name, Gender.MALE, "", "",
                    "", "", "", documentIDNumber, currentInstitution);

            externalPersons.add(externalPerson);
        }

        return externalPersons;
    }

}
