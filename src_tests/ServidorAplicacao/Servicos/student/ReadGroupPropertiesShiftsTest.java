/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShift;
import DataBeans.InfoSiteShifts;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadGroupPropertiesShiftsTest extends TestCaseReadServices {

	public ReadGroupPropertiesShiftsTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadGroupPropertiesShiftsTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadGroupPropertiesShifts";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object[] result = { new Integer(6),"15"};
		return result;
	
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(3),"15"};
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	protected Object getObjectToCompare() {
		InfoSiteShifts infoSiteShifts = new InfoSiteShifts();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			ITurno shift =(ITurno) sp.getITurnoPersistente().readByOId(
								new Turno(new Integer(9)),
								false);
								
			InfoShift infoShift = Cloner.copyIShift2InfoShift(shift);
			infoShift.setIdInternal(shift.getIdInternal());
			 									
			List infoShifts = new ArrayList();
			infoShifts.add(infoShift);
			infoSiteShifts.setShifts(infoShifts);
			
			infoSiteShifts.setInfoExecutionPeriodName("2º Semestre");
			infoSiteShifts.setInfoExecutionYearName("2002/2003");
										
			
			sp.confirmarTransaccao();
		}catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();;
		}
		System.out.println("-----------no teste-tam-shifts"+infoSiteShifts.getShifts().size());
		System.out.println("-----------tam-shifts"+infoSiteShifts.getShifts().get(0).toString());
			

		System.out.println("-----------tam-ano"+infoSiteShifts.getInfoExecutionYearName());

		System.out.println("-----------tam-periodo"+infoSiteShifts.getInfoExecutionPeriodName());
		return infoSiteShifts;
		
	}

}
