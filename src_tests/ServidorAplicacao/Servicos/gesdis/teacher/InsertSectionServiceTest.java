/*
 * Created on 3/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class InsertSectionServiceTest extends TestCaseCreateServices {

	public InsertSectionServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(InsertSectionServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertSection";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {


		
		Object argsInsertSection[] = new Object[1];
	
		ISuportePersistente sp = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		IPersistentSection persistentSection = null;
		ISite site = null;
		
				  
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite isp = sp.getIPersistentSite();
			persistentSection = sp.getIPersistentSection();

			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod().readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("PO", executionPeriod);
//			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","LEEC");
		
			site= isp.readByExecutionCourse(executionCourse);
			
			System.out.println("ARGS INTERNALCODE DO SITE"+site.toString());
			sp.confirmarTransaccao();	
		
			} catch (ExcepcaoPersistencia e) {
				
				System.out.println("failed setting up the test data");
			
			}
			
			
			ISection section = new Section("NovaSeccaoDePO", site,null);
			 
			InfoSection infoSection = Cloner.copyISection2InfoSection(section);
			infoSection.setSectionOrder(new Integer(0));
			
			argsInsertSection[0] =infoSection;
	
			return argsInsertSection;
			
		
	}



	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		
		Object argsInsertSection[] = new Object[1];
	
		ISuportePersistente sp = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		IPersistentSection persistentSection = null;
		ISite site = null;
		
				  
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite isp = sp.getIPersistentSite();
			persistentSection = sp.getIPersistentSection();
			
			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod().readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("PO", executionPeriod);
		
			site = isp.readByExecutionCourse(executionCourse);

			sp.confirmarTransaccao();	
		
			} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			}
			
			InfoSite infoSite = Cloner.copyISite2InfoSite(site);
			
			argsInsertSection[0] = new InfoSection("Seccao1dePO",new Integer(0),infoSite);
	
			return argsInsertSection;
			
	
		}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}