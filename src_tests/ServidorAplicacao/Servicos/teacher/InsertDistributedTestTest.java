/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTestTest extends TestCaseDeleteAndEditServices {

	public InsertDistributedTestTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertDistributedTest";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		IDistributedTest distributedTest = new DistributedTest(new Integer(25));
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentDistributedTest persistentDistributedTest =
				sp.getIPersistentDistributedTest();
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest,
					false);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("excepcao");
		}
		String[] selected = { new String("19")};
		Object[] args =
			{
				new Integer(26),
				new Integer(3),
				new String("Informação do teste"),
				distributedTest.getBeginDate(),
				distributedTest.getBeginHour(),
				distributedTest.getEndDate(),
				distributedTest.getEndHour(),
				distributedTest.getTestType(),
				distributedTest.getCorrectionAvailability(),
				distributedTest.getStudentFeedback(),
				selected,
				new Boolean(true)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		IDistributedTest distributedTest = new DistributedTest(new Integer(25));
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentDistributedTest persistentDistributedTest =
				sp.getIPersistentDistributedTest();
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest,
					false);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("excepcao");
		}
		String[] selected = { new String("19")};
		Object[] args =
			{
				new Integer(26),
				new Integer(0),
				new String("Informação do teste"),
				distributedTest.getBeginDate(),
				distributedTest.getBeginHour(),
				distributedTest.getEndDate(),
				distributedTest.getEndHour(),
				distributedTest.getTestType(),
				distributedTest.getCorrectionAvailability(),
				distributedTest.getStudentFeedback(),
				selected,
				new Boolean(true)};
		return args;
	}

}
