/*
 * Created on 23/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class VerifyIfCanDeleteDistributedTest extends Service {
	public boolean run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		DistributedTest distributedTest = (DistributedTest) persistentSuport
				.getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
		if (distributedTest == null) {
			throw new InvalidArgumentsServiceException();
		}
		if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
			if (persistentSuport.getIPersistentStudentTestQuestion().countResponsedOrNotResponsed(null,
					true, distributedTestId) != 0)
				return false;
		}
		return true;
	}
}