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
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.util.Cloner;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentGroupsTest extends TestCaseReadServices {

	public ReadStudentGroupsTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadStudentGroupsTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentGroups";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(5),new Integer(34)};
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	protected Object getObjectToCompare() {
		InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudentGroup studentGroup =(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(
														new StudentGroup(new Integer(11)),
														false);
			ITurno shift =(ITurno) sp.getITurnoPersistente().readByOId(
																	new Turno(new Integer(34)),
																	false);											
			List infoStudentGroupsList = new ArrayList();
			InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
			infoSiteStudentGroup.setNrOfElements(new Integer(3));
			infoSiteStudentGroup.setInfoStudentGroup(Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup));
			infoStudentGroupsList.add(infoSiteStudentGroup);
			infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoStudentGroupsList);
			
			InfoSiteShift infoSiteShift = new InfoSiteShift();
			infoSiteShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
			infoSiteShift.setNrOfGroups(new Integer(9));
			infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
			
									
			
			sp.confirmarTransaccao();
		}catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();;
		}
		return infoSiteGroupsByShift;
	
		
	}

}
