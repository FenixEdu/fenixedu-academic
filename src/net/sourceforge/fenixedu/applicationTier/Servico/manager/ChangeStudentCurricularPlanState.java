/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ChangeStudentCurricularPlanState implements IService {

    public void run(final Integer studentCurricularPlanId,
            final StudentCurricularPlanState studentCurricularPlanState) throws ExcepcaoPersistencia, DomainException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = 
					persistentSupport.getIStudentCurricularPlanPersistente();

		final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);
		
		studentCurricularPlan.changeState(studentCurricularPlanState);
    }

}
