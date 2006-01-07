package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExternalPerson implements IService {

    public void run(Integer externalPersonID, String name, String address, Integer institutionID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ExternalPerson storedExternalPerson = (ExternalPerson) sp.getIPersistentExternalPerson()
                .readByOID(ExternalPerson.class, externalPersonID);

        if (storedExternalPerson == null)
            throw new NonExistingServiceException(
                    "error.exception.externalPerson.nonExistingExternalPsrson");

        List<ExternalPerson> allExternalPersons = (List<ExternalPerson>) sp
                .getIPersistentExternalPerson().readAll(ExternalPerson.class);

        Institution storedInstitution = (Institution) sp.getIPersistentInstitution().readByOID(
                Institution.class, institutionID);

        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedInstitution,
                allExternalPersons);
    }
}