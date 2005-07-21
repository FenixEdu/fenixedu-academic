package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.areas;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in Apr 14, 2004
 */

public class WriteStudentAreasWithoutRestrictions implements IService {
    public WriteStudentAreasWithoutRestrictions() {
    }

    // The first 2 arguments are only used by the filter applyed to this
    // service.
    public void run(InfoStudent infoStudent, DegreeType degreeType, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

            IBranch specializationArea = (IBranch) branchDAO.readByOID(Branch.class, specializationAreaID);
            IBranch secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);

            if (studentCurricularPlan == null) {
                throw new NonExistingServiceException();
            }

            if (specializationArea != null && secundaryArea != null
                    && specializationArea.equals(secundaryArea)) {
                throw new BothAreasAreTheSameServiceException();
            }

            studentCurricularPlan.setBranch(specializationArea);
            studentCurricularPlan.setSecundaryBranch(secundaryArea);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

}