package ServidorAplicacao.Servico.enrollment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedBranchChangeException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas implements IService {
    public WriteStudentAreas() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentBranch branchDAO = persistentSuport
                    .getIPersistentBranch();
            IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class,
                            studentCurricularPlanID);

            IBranch specializationArea = (IBranch) branchDAO.readByOID(
                    Branch.class, specializationAreaID);

            IBranch secundaryArea = null;
            if (secundaryAreaID != null) {
                secundaryArea = (IBranch) branchDAO.readByOID(Branch.class,
                        secundaryAreaID);
            }

            if (studentCurricularPlan == null) {
                throw new ExistingServiceException();
            }
            if (!studentCurricularPlan.getCanChangeSpecializationArea()){
                throw new NotAuthorizedBranchChangeException();
            }
           
                if (studentCurricularPlan.areNewAreasCompatible(
                        specializationArea, secundaryArea)) {
                    studentCurricularPlanDAO
                            .simpleLockWrite(studentCurricularPlan);
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