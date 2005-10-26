package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class EditInstitution implements IService {

    private static EditInstitution service = new EditInstitution();

    /**
     * The singleton access method of this class.
     */
    public static EditInstitution getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private EditInstitution() {
    }

    public void run(Integer oldInstitutionOID, String newInstitutionName) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IInstitution storedInstitution = sp.getIPersistentInstitution().readByName(
                    newInstitutionName);
            IInstitution oldInstitution = (IInstitution) sp.getIPersistentInstitution().readByOID(
                    Institution.class, oldInstitutionOID);

            if (oldInstitution == null) {
                throw new NonExistingServiceException(
                        "error.exception.commons.institution.institutionNotFound");
            }

            if ((storedInstitution != null) && (!storedInstitution.equals(oldInstitution))) {
                throw new ExistingServiceException(
                        "error.exception.commons.institution.institutionAlreadyExists");
            }

            oldInstitution.setName(newInstitutionName);
            sp.getIPersistentInstitution().simpleLockWrite(oldInstitution);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}