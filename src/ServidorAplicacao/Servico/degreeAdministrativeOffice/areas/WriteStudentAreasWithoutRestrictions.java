package ServidorAplicacao.Servico.degreeAdministrativeOffice.areas;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos in Apr 14, 2004
 */

public class WriteStudentAreasWithoutRestrictions implements IService {
    public WriteStudentAreasWithoutRestrictions() {
    }

    // The first 2 arguments are only used by the filter applyed to this
    // service.
    public void run(InfoStudent infoStudent, TipoCurso degreeType, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
            IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

            IBranch specializationArea = (IBranch) branchDAO.readByOID(Branch.class,
                    specializationAreaID);

            IBranch secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);

            if (studentCurricularPlan == null) {
                throw new NonExistingServiceException();
            }

            if (specializationArea != null && secundaryArea != null
                    && specializationArea.equals(secundaryArea)) {
                throw new BothAreasAreTheSameServiceException();
            }

            studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);
            studentCurricularPlan.setBranch(specializationArea);
            studentCurricularPlan.setSecundaryBranch(secundaryArea);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

}