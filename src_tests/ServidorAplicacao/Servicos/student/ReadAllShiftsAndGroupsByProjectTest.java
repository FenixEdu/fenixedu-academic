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
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.util.Cloner;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAllShiftsAndGroupsByProjectTest extends TestCaseReadServices {

	public ReadAllShiftsAndGroupsByProjectTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadAllShiftsAndGroupsByProjectTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadAllShiftsAndGroupsByProject";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object[] result = { new Integer(4)};
		return result;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(6)};
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	protected Object getObjectToCompare() {
		InfoSiteAllGroups infoSiteAllGroups = new InfoSiteAllGroups();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			ITurno shift =(ITurno) sp.getITurnoPersistente().readByOId(
								new Turno(new Integer(30)),
								false);
			List allInfoStudentGroup = new ArrayList();
			
			IStudentGroup studentGroup =(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(
											new StudentGroup(new Integer(10)),
											false);
			allInfoStudentGroup.add(Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup));								
			
			InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
			infoSiteGroupsByShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
			infoSiteGroupsByShift.setInfoStudentGroupsList(allInfoStudentGroup);
			
			
			List allInfoSiteGroupsByShift=new ArrayList();
			allInfoSiteGroupsByShift.add(infoSiteGroupsByShift);
			infoSiteAllGroups.setInfoSiteGroupsByShiftList(allInfoSiteGroupsByShift); 							
			
			sp.confirmarTransaccao();
		}catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();;
		}
		return infoSiteAllGroups;
		
	}

}
