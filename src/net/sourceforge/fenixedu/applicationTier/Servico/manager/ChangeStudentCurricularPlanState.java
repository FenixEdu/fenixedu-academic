/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ChangeStudentCurricularPlanState extends Service {

    public void run(final Integer studentCurricularPlanId,
            final StudentCurricularPlanState studentCurricularPlanState) throws ExcepcaoPersistencia, DomainException {
		final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);		
		studentCurricularPlan.changeState(studentCurricularPlanState);
    }

}
