package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IInstitution;
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

        IExternalPerson storedExternalPerson = (IExternalPerson) sp.getIPersistentExternalPerson()
                .readByOID(ExternalPerson.class, externalPersonID);

        if (storedExternalPerson == null)
            throw new NonExistingServiceException(
                    "error.exception.externalPerson.nonExistingExternalPsrson");

        List<IExternalPerson> allExternalPersons = (List<IExternalPerson>) sp
                .getIPersistentExternalPerson().readAll(ExternalPerson.class);

        IInstitution storedInstitution = (IInstitution) sp.getIPersistentInstitution().readByOID(
                Institution.class, institutionID);

        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedInstitution,
                allExternalPersons);
    }
}