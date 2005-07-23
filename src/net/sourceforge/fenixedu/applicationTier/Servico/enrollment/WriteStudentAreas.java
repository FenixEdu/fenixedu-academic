package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas implements IService {
    public WriteStudentAreas() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID) throws FenixServiceException {
    
		try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSuport.getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

            if (studentCurricularPlan == null) {
                throw new NonExistingServiceException();
            }
			
            IBranch specializationArea = (IBranch) persistentBranch.readByOID(Branch.class, specializationAreaID);

            IBranch secundaryArea = null;
            if (secundaryAreaID != null) {
                secundaryArea = (IBranch) persistentBranch.readByOID(Branch.class, secundaryAreaID);
            }

			
			try {
				studentCurricularPlan.setStudentAreas(specializationArea, secundaryArea);
			}
			catch (DomainException e) {
				throw new ChosenAreasAreIncompatibleServiceException();
			}

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}