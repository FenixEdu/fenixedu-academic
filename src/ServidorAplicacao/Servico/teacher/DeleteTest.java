/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.IDistributedTest;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteTest implements IServico {

	private static DeleteTest service = new DeleteTest();

	public static DeleteTest getService() {

		return service;
	}

	public DeleteTest() {
	}

	public String getNome() {
		return "DeleteTest";
	}

	public boolean run(Integer executionCourseId, Integer testId)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, true);
			if (test == null)
				throw new FenixServiceException();

			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();

			List distributedTestList =
				(List) persistentDistributedTest.readByTest(test);

			Calendar thisDate = Calendar.getInstance();
			boolean setVisibility = false;
			Iterator it = distributedTestList.iterator();
			while (it.hasNext()) {
				IDistributedTest distributedTest = (IDistributedTest) it.next();
				if (compareDates(thisDate,
					distributedTest.getBeginDate(),
					distributedTest.getBeginHour())
					== true) {
					setVisibility = true;
					if (!compareDates(thisDate,
						distributedTest.getEndDate(),
						distributedTest.getEndHour())) {
						persistentDistributedTest.lockWrite(distributedTest);
						distributedTest.setEndDate(thisDate);
						distributedTest.setEndHour(thisDate);
					}
				} else {
					persistentSuport.getIPersistentStudentTestQuestion().deleteByDistributedTest(distributedTest);
					persistentDistributedTest.delete(distributedTest);
				}

			}

			if (setVisibility == false) {
				persistentDistributedTest.deleteByTest(test);
				IPersistentTestQuestion persistentTestQuestion =
					persistentSuport.getIPersistentTestQuestion();
				persistentTestQuestion.deleteByTest(test);
				persistentTest.delete(test);
			} else
				test.setVisible(new Boolean(false));
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private boolean compareDates(
		Calendar calendar,
		Calendar date,
		Calendar hour) {
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) >= 0) {
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) >= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}
}
