/*
 * Created on Feb 18, 2005
 *
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;

/**
 * @author Luis Cruz
 * 
 */
public class ChangeStudentCurricularPlanState implements IService {

    public void run(final Integer studentCurricularPlanId,
            final StudentCurricularPlanState studentCurricularPlanState) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

        persistentStudentCurricularPlan.simpleLockWrite(studentCurricularPlan);

        studentCurricularPlan.setCurrentState(studentCurricularPlanState);
    }

}
