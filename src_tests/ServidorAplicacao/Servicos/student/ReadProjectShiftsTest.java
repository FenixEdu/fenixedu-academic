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
import DataBeans.InfoSiteProjectShifts;
import DataBeans.InfoSiteShift;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadProjectShiftsTest extends TestCaseReadServices {

	public ReadProjectShiftsTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadProjectShiftsTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadProjectShifts";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object[] result = { new Integer(1)};
		return result;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(6)};
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 2;
	}

	protected Object getObjectToCompare() {
		InfoSiteProjectShifts infoSiteProjectShifts = new InfoSiteProjectShifts();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			ITurno shift =(ITurno) sp.getITurnoPersistente().readByOId(
								new Turno(new Integer(30)),
								false);
			List infoShiftsList = new ArrayList();
			InfoSiteShift infoSiteShift = new InfoSiteShift();
			infoSiteShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
			infoSiteShift.setNrOfGroups(new Integer(9));
			infoShiftsList.add(infoSiteShift);
			
			
			
			infoSiteProjectShifts.setInfoSiteShifts(infoShiftsList);
			
			
			

			sp.confirmarTransaccao();
		}catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();;
		}
		return infoSiteProjectShifts;
	//return null;
		
	}

}
