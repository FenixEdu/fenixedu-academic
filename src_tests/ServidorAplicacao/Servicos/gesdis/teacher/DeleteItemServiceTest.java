/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.gesdis.InfoItem;
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
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class DeleteItemServiceTest extends TestCaseDeleteAndEditServices {

	public DeleteItemServiceTest(String testName) {
		super(testName);		
	}
	
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DeleteItemServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteItem";
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
			
			IPersistentItem ipItem=sp.getIPersistentItem();
			item = ipItem.readBySectionAndName(section,"Item1deTFCI");
			sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				System.out.println("failed setting up the test data");
				e.printStackTrace();
			}

		InfoItem infoItem = Cloner.copyIItem2InfoItem(item);
		Object[] argsDeleteItem = {infoItem};
		
		return argsDeleteItem;		
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
