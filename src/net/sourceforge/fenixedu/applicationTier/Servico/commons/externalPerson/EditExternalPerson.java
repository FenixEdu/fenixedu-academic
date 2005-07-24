package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExternalPerson implements IService {

    public void run(Integer externalPersonID, String name, String address, Integer workLocationID,
            String phone, String mobile, String homepage, String email) throws FenixServiceException, ExcepcaoPersistencia {
        IExternalPerson storedExternalPerson = null;
        IExternalPerson existingExternalPerson = null;

        IWorkLocation storedWorkLocation = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        storedExternalPerson = (IExternalPerson) sp.getIPersistentExternalPerson().readByOID(
                ExternalPerson.class, externalPersonID);
        
        if (storedExternalPerson == null)
            throw new NonExistingServiceException(
                    "error.exception.externalPerson.nonExistingExternalPsrson");

        List<IExternalPerson> allExternalPersons = (List<IExternalPerson>) sp.getIPersistentExternalPerson().readAll(ExternalPerson.class);

        storedWorkLocation = (IWorkLocation) sp.getIPersistentWorkLocation().readByOID(WorkLocation.class, workLocationID);
        
        storedExternalPerson.edit(name, address, phone, mobile, homepage, email, storedWorkLocation, allExternalPersons);
    }
}