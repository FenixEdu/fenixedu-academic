/*
 * Created on 1/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest implements IServico {
	private static EditDistributedTest service = new EditDistributedTest();

	public static EditDistributedTest getService() {
		return service;
	}

	public EditDistributedTest() {
	}

	public String getNome() {
		return "EditDistributedTest";
	}

	public boolean run(
		Integer executionCourseId,
		Integer distributedTestId,
		String testInformation,
		Calendar beginDate,
		Calendar beginHour,
		Calendar endDate,
		Calendar endHour,
		TestType testType,
		CorrectionAvailability correctionAvailability,
		Boolean studentFeedback)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();

			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest,
					true);

			distributedTest.setTestInformation(testInformation);
			distributedTest.setBeginDate(beginDate);
			distributedTest.setBeginHour(beginHour);
			distributedTest.setEndDate(endDate);
			distributedTest.setEndHour(endHour);
			distributedTest.setTestType(testType);
			distributedTest.setCorrectionAvailability(correctionAvailability);
			distributedTest.setStudentFeedback(studentFeedback);

			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
