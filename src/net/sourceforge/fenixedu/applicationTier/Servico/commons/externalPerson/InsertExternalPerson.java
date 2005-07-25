package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
        String documentIdNumber = String.valueOf(nextID);

        IExternalPerson externalPerson = new ExternalPerson(name, Gender.valueOf(sex), address, phone, mobile, homepage, email, documentIdNumber, storedWorkLocation);        
        
        return externalPerson;

    }
}