/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
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
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		
        Iterator iterator = enrolmentsIds.iterator();
		for (Integer enrolmentId : enrolmentsIds) {
            
            IEnrolment enrolment = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class,enrolmentId);
            if (enrolment == null) {
                throw new InvalidArgumentsServiceException();
            }

			IEnrolmentEvaluation improvmentEnrolmentEvaluation = enrolment.getImprovementEvaluation();
            if (improvmentEnrolmentEvaluation != null) {
				try {
					improvmentEnrolmentEvaluation.unEnrollImprovment(persistentExecutionPeriod.readActualExecutionPeriod());
				}
				catch (DomainException e) {}
				
            }
        }

        return new Boolean(true);

    }

}
