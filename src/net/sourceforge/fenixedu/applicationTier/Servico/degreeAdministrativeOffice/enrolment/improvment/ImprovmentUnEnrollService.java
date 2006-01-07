/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 */
public class ImprovmentUnEnrollService implements IService {

    public Object run(Integer studentNumber, List<Integer> enrolmentsIds)
			throws FenixServiceException, ExcepcaoPersistencia, DomainException {
        
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		
		for (Integer enrolmentId : enrolmentsIds) {
            
            Enrolment enrolment = (Enrolment) persistentEnrollment.readByOID(Enrolment.class,enrolmentId);
            if (enrolment == null) {
                throw new InvalidArgumentsServiceException();
            }

			enrolment.unEnrollImprovement(persistentExecutionPeriod.readActualExecutionPeriod());
        }

        return new Boolean(true);
    }
}
