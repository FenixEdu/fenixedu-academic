/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ImprovmentUnEnrollService extends Service {

    public Object run(Integer studentNumber, List<Integer> enrolmentsIds)
			throws FenixServiceException, ExcepcaoPersistencia, DomainException {
		
		for (Integer enrolmentId : enrolmentsIds) {
            final Enrolment enrolment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrolmentId);
            if (enrolment == null) {
                throw new InvalidArgumentsServiceException();
            }
			enrolment.unEnrollImprovement(ExecutionPeriod.readActualExecutionPeriod());
        }

        return new Boolean(true);
    }
}
