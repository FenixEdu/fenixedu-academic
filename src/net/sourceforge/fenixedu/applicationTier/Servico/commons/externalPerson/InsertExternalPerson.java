package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertExternalPerson implements IService {

    public IExternalPerson run(String name, String sex, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IExternalPerson storedExternalPerson = sp.getIPersistentExternalPerson()
                .readByNameAndAddressAndInstitutionID(name, address, institutionID);

        if (storedExternalPerson != null)
            throw new ExistingServiceException(
                    "error.exception.commons.externalPerson.existingExternalPerson");

        IInstitution institutionLocation = (IInstitution) sp.getIPersistentInstitution().readByOID(
                Institution.class, institutionID);

        // generate new identification number
        String lastDocumentIdNumber = sp.getIPersistentExternalPerson().readLastDocumentIdNumber();
        int nextID = Integer.parseInt(lastDocumentIdNumber) + 1;
        String documentIdNumber = String.valueOf(nextID);

        return DomainFactory.makeExternalPerson(name, Gender.valueOf(sex), address, phone, mobile,
                homepage, email, documentIdNumber, institutionLocation);
    }

}
