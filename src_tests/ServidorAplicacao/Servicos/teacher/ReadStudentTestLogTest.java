/*
 * Created on 19/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoStudentTestLog;
import DataBeans.util.Cloner;
import Dominio.IStudentTestLog;
import Dominio.StudentTestLog;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestLogTest extends TestCaseReadServices {

	/**
	* @param testName
	*/
	public ReadStudentTestLogTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentTestLog";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26), new Integer(27), new Integer(9)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 10;
	}

	protected Object getObjectToCompare() {
		List logList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudentTestLog log1 = new StudentTestLog(new Integer(1));
			IStudentTestLog log2 = new StudentTestLog(new Integer(2));
			IStudentTestLog log3 = new StudentTestLog(new Integer(3));
			IStudentTestLog log4 = new StudentTestLog(new Integer(4));
			IStudentTestLog log5 = new StudentTestLog(new Integer(5));
			IStudentTestLog log6 = new StudentTestLog(new Integer(6));
			IStudentTestLog log7 = new StudentTestLog(new Integer(7));
			IStudentTestLog log8 = new StudentTestLog(new Integer(8));
			IStudentTestLog log9 = new StudentTestLog(new Integer(9));
			IStudentTestLog log10 = new StudentTestLog(new Integer(10));

			log1 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log1,
					false);
			log2 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log2,
					false);
			log3 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log3,
					false);
			log4 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log4,
					false);
			log5 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log5,
					false);
			log6 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log6,
					false);
			log7 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log7,
					false);
			log8 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log8,
					false);
			log9 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log9,
					false);
			log10 =
				(IStudentTestLog) sp.getIPersistentStudentTestLog().readByOId(
					log10,
					false);

			assertNotNull("log1 null", log1);
			assertNotNull("log2 null", log2);
			assertNotNull("log3 null", log3);
			assertNotNull("log4 null", log4);
			assertNotNull("log5 null", log5);
			assertNotNull("log6 null", log6);
			assertNotNull("log7 null", log7);
			assertNotNull("log8 null", log8);
			assertNotNull("log9 null", log9);
			assertNotNull("log10 null", log10);

			sp.confirmarTransaccao();

			InfoStudentTestLog infoLog1 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log1);
			InfoStudentTestLog infoLog2 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log2);
			InfoStudentTestLog infoLog3 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log3);
			InfoStudentTestLog infoLog4 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log4);
			InfoStudentTestLog infoLog5 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log5);
			InfoStudentTestLog infoLog6 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log6);
			InfoStudentTestLog infoLog7 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log7);
			InfoStudentTestLog infoLog8 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log8);
			InfoStudentTestLog infoLog9 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log9);
			InfoStudentTestLog infoLog10 =
				Cloner.copyIStudentTestLog2InfoStudentTestLog(log10);

			logList.add(infoLog1);
			logList.add(infoLog2);
			logList.add(infoLog3);
			logList.add(infoLog4);
			logList.add(infoLog5);
			logList.add(infoLog6);
			logList.add(infoLog7);
			logList.add(infoLog8);
			logList.add(infoLog9);
			logList.add(infoLog10);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		return logList;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}
