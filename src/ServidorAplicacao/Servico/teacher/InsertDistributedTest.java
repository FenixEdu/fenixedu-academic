/*
 * Created on 19/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTest implements IServico {
	private static InsertDistributedTest service = new InsertDistributedTest();

	public static InsertDistributedTest getService() {
		return service;
	}

	public InsertDistributedTest() {
	}

	public String getNome() {
		return "InsertDistributedTest";
	}

	public boolean run(
		Integer testId,
		Calendar beginDate,
		Calendar beginHour,
		Calendar endDate,
		Calendar endHour,
		TestType testType,
		CorrectionAvailability correctionAvaiability,
		Boolean feedback)
		throws FenixServiceException {
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			IDistributedTest distributedTest = new DistributedTest();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}

			distributedTest.setBeginDate(beginDate);
			distributedTest.setBeginHour(beginHour);
			distributedTest.setEndDate(endDate);
			distributedTest.setEndHour(endHour);
			distributedTest.setTestType(testType);
			distributedTest.setCorrectionAvailability(correctionAvaiability);
			distributedTest.setStudentFeedback(feedback);
			distributedTest.setTest(test);

			persistentDistributedTest.simpleLockWrite(distributedTest);
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
