package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

            IBranch specializationArea = (IBranch) branchDAO.readByOID(Branch.class,
                    specializationAreaID);

            IBranch secundaryArea = null;
            if (secundaryAreaID != null) {
                secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);
            }

            if (studentCurricularPlan == null) {
                throw new ExistingServiceException();
            }
            if (!studentCurricularPlan.getCanChangeSpecializationArea()) {
                throw new NotAuthorizedBranchChangeException();
            }

            if (studentCurricularPlan.areNewAreasCompatible(specializationArea, secundaryArea)) {
                studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);
                studentCurricularPlan.setBranch(specializationArea);
                studentCurricularPlan.setSecundaryBranch(secundaryArea);
            } else {
                throw new ChosenAreasAreIncompatibleServiceException();
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}