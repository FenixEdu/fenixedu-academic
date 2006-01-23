/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan extends Service {

    public void run(final Integer studentCurricularPlanId) throws DomainException, ExcepcaoPersistencia, NonExistingServiceException {
        final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

		if (studentCurricularPlan != null)
			studentCurricularPlan.delete();
		else
			throw new NonExistingServiceException();
    }
}