package ServidorAplicacao.Servicos.teacher;

import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 */
public class InsertSummaryTest extends TestCaseDeleteAndEditServices {

	public InsertSummaryTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertSummary";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISummary summary = new Summary(new Integer(261));
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			summary = (ISummary) persistentSummary.readByOId(summary, false);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
		fail("excepcao");
		}
		Object[] argsDeleteItem =
			{
				new Integer(25),
				summary.getSummaryDate(),
				summary.getSummaryHour(),
				summary.getSummaryType().getTipo(),
				summary.getTitle(),
				summary.getSummaryText()};
		
		return argsDeleteItem;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
