/*
 * Created on 14/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr e scpo
 */


public class InsertItemTest extends TestCaseCreateServices {

	public InsertItemTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(InsertItemTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertItem";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {


		
		Object argsInsertItem[] = new Object[1];
	
		ISuportePersistente sp = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		IPersistentSection persistentSection = null;
		ISection section = null;
		
				  
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite isp = sp.getIPersistentSite();
			persistentSection = sp.getIPersistentSection();
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","LEEC");
			
		
			ISite site= isp.readByExecutionCourse(executionCourse);
			
			section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1dePO");
			
			sp.confirmarTransaccao();	
		
			} catch (ExcepcaoPersistencia e) {
				
				System.out.println("failed setting up the test data");
			
			}
			
			InfoSection infoSection = Cloner.copyISection2InfoSection(section);

			argsInsertItem[0] = new InfoItem("item0 da seccao1dePO","Item0",new Integer(0),infoSection,new Boolean(true));
	
			return argsInsertItem;

			
		
	}



	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		
		Object argsInsertItem[] = new Object[1];
	
		ISuportePersistente sp = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IDisciplinaExecucao executionCourse = null;
		IPersistentSection persistentSection = null;
		ISection section = null;
		
				  
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite isp = sp.getIPersistentSite();
			persistentSection = sp.getIPersistentSection();
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","LEEC");
		
			ISite site= isp.readByExecutionCourse(executionCourse);

			section = persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1dePO");
			
			sp.confirmarTransaccao();	
		
			} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			}
			
			InfoSection infoSection =Cloner.copyISection2InfoSection(section);
			
			argsInsertItem[0] = new InfoItem("item1 da seccao1dePO","Item1dePO",new Integer(1),infoSection,new Boolean(true));
	
			return argsInsertItem;
			
	
		}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}