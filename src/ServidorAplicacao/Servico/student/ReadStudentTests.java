/*
 * Created on 27/Ago/2003
 *
 */

package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteStudentDistributedTests;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTests implements IService {

	public ReadStudentTests() {
	}

	public Object run(String userName, Integer executionCourseId)
			throws FenixServiceException {
		InfoSiteStudentDistributedTests infoSite = new InfoSiteStudentDistributedTests();
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IStudent student = persistentSuport.getIPersistentStudent()
					.readByUsername(userName);

			IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
					.getIPersistentExecutionCourse().readByOID(
							ExecutionCourse.class, executionCourseId);

			List distributedTestList = persistentSuport
					.getIPersistentDistributedTest()
					.readByStudentAndExecutionCourse(student, executionCourse);
			List testToDoList = new ArrayList();
			List doneTestsList = new ArrayList();
			Iterator it = distributedTestList.iterator();
			while (it.hasNext()) {
				IDistributedTest distributedTest = (IDistributedTest) it.next();
				if (testsToDo(distributedTest)) {
					InfoDistributedTest infoDistributedTest = InfoDistributedTest
							.newInfoFromDomain(distributedTest);
					if (!testToDoList.contains(infoDistributedTest))
						testToDoList.add(infoDistributedTest);
				} else if (doneTests(distributedTest)) {
					InfoDistributedTest infoDistributedTest = InfoDistributedTest
							.newInfoFromDomain(distributedTest);
					if (!doneTestsList.contains(infoDistributedTest))
						doneTestsList.add(infoDistributedTest);
				}

			}
			infoSite.setInfoDistributedTestsToDo(testToDoList);
			infoSite.setInfoDoneDistributedTests(doneTestsList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoSite;
	}

	private boolean testsToDo(IDistributedTest distributedTest) {
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();

		if (dateComparator.compare(calendar, distributedTest.getBeginDate()) >= 0) {
			if (dateComparator
					.compare(calendar, distributedTest.getBeginDate()) == 0)
				if (hourComparator.compare(calendar, distributedTest
						.getBeginHour()) < 0)
					return false;
			if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
				if (dateComparator.compare(calendar, distributedTest
						.getEndDate()) == 0)
					if (hourComparator.compare(calendar, distributedTest
							.getEndHour()) <= 0)
						return true;
					else
						return false;
				return true;
			}
		}
		return false;
	}

	private boolean doneTests(IDistributedTest distributedTest) {
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
			if (dateComparator.compare(calendar, distributedTest.getEndDate()) == 0)
				if (hourComparator.compare(calendar, distributedTest
						.getEndHour()) <= 0)
					return false;
				else
					return true;
			return false;
		}
		return true;
	}
}