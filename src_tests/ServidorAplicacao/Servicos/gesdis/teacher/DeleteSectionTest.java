/*
 * Created on 27/Mar/2003
 */
package ServidorAplicacao.Servicos.gesdis.teacher;
/**
 * @author lmac1
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteSectionTest extends TestCaseDeleteAndEditServices {

	public DeleteSectionTest(String testName) {
		super(testName);		
	}
	
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DeleteSectionTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteSection";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		ISite site = null;
		ISection section = null;
		IItem item = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp =sp.getIPersistentExecutionPeriod();
			executionPeriod =iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep =sp.getIDisciplinaExecucaoPersistente();
			executionCourse =idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",executionPeriod);

			IPersistentSite ipSite =sp.getIPersistentSite();
			site =ipSite.readByExecutionCourse(executionCourse);
				
			IPersistentSection ipSection =sp.getIPersistentSection();
			section =ipSection.readBySiteAndSectionAndName(site,null,"Seccao1deTFCI");
			
			sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				System.out.println("failed setting up the test data");
				e.printStackTrace();
			}

		InfoSection infoSection = Cloner.copyISection2InfoSection(section);		
		Object[] argsDeleteSection = {infoSection};
		
		return argsDeleteSection;		
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}