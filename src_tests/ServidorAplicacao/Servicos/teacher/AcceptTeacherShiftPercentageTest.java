/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRole;
import DataBeans.InfoShift;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.IShiftProfessorship;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Teacher;
import Dominio.Turno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class AcceptTeacherShiftPercentageTest extends TestCaseServices {

	/**
	 * @param testName
	 */
	public AcceptTeacherShiftPercentageTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "AcceptTeacherExecutionCourseShiftPercentage";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForTeacherCredits.xml";
	}

	public static void main(java.lang.String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(WriteCreditsTeacherTest.class);
		return testSuite;
	}

	public void testAlterShiftPercentage() {
		InfoTeacher infoTeacher = getInfoTeacher(new Integer(1));

		Object[][] shiftPercentageArray = { { new Integer(2), new Double(50)}
		};
		List infoTeacherShiftPercentageList = getInfoTeacherShiftPercentageList(shiftPercentageArray);

		Object[] args = { infoTeacher, getInfoExecutionCourse(new Integer(1)), infoTeacherShiftPercentageList };

		try {
			ServiceUtils.executeService(getUserViewAuthorized(), getNameOfServiceToBeTested(), args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing service!");
		}

		testProfessorShip(new Integer(1), new Integer(2));

		testPercentage(new Integer(2), new Double(50));
	}

	public void testShiftNotFull() {
		InfoTeacher infoTeacher = getInfoTeacher(new Integer(1));

		Object[][] shiftPercentageArray = { { new Integer(3), new Double(50)}
		};
		List infoTeacherShiftPercentageList = getInfoTeacherShiftPercentageList(shiftPercentageArray);

		Object[] args = { infoTeacher, getInfoExecutionCourse(new Integer(1)), infoTeacherShiftPercentageList };

		try {
			ServiceUtils.executeService(getUserViewAuthorized(), getNameOfServiceToBeTested(), args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Shouldn't percentage exceed!");
		}

		testPercentage(new Integer(3), new Double(50));

		infoTeacher = getInfoTeacher(new Integer(2));

		Object[][] shiftPercentageArray2 = { { new Integer(3), new Double(50)}
		};
		infoTeacherShiftPercentageList = getInfoTeacherShiftPercentageList(shiftPercentageArray2);

		Object[] args2 = { infoTeacher, getInfoExecutionCourse(new Integer(1)), infoTeacherShiftPercentageList };

		try {
			ServiceUtils.executeService(getUserViewAuthorized(), getNameOfServiceToBeTested(), args2);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing Service!");
		}

		testPercentage(new Integer(3), new Double(100));
	}

	public void testShiftFull() {
		InfoTeacher infoTeacher = getInfoTeacher(new Integer(1));

		Object[][] shiftPercentageArray = { { new Integer(1), new Double(50)}
		};
		List infoTeacherShiftPercentageList = getInfoTeacherShiftPercentageList(shiftPercentageArray);

		Object[] args = { infoTeacher, getInfoExecutionCourse(new Integer(1)), infoTeacherShiftPercentageList };

		try {
			List shiftWithErrors = (List) ServiceUtils.executeService(getUserViewAuthorized(), getNameOfServiceToBeTested(), args);
			//			fail("Percentage should exceed!");

			assertEquals("Size is not the same on shiftWithErrors!", 1, shiftWithErrors.size());
			assertEquals(
				"shiftWithErrors whith shift 1",
				1,
				((ITurno) (Cloner.copyInfoShift2IShift((InfoShift) shiftWithErrors.get(0)))).getIdInternal().intValue());
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing Service!");
		}
	}

	public void testDelete() {
		InfoTeacher infoTeacher = getInfoTeacher(new Integer(1));

		Object[][] shiftPercentageArray = { { new Integer(2), new Double(0)}
		};
		List infoTeacherShiftPercentageList = getInfoTeacherShiftPercentageList(shiftPercentageArray);

		Object[] args = { infoTeacher, getInfoExecutionCourse(new Integer(1)), infoTeacherShiftPercentageList };

		try {
			ServiceUtils.executeService(getUserViewAuthorized(), getNameOfServiceToBeTested(), args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing service!");
		}

		testDeleted(infoTeacherShiftPercentageList);
	}

	private void testDeleted(List infoTeacherShiftPercentageList) {
		//TODO:
		ISuportePersistente sp;
		try {
			sp = SuportePersistenteOJB.getInstance();

			IPersistentShiftProfessorship teacherShiftPercentageDAO = sp.getIPersistentTeacherShiftPercentage();
			IShiftProfessorship teacherShiftPercentage = null;

			Iterator iterator = infoTeacherShiftPercentageList.listIterator();
			while (iterator.hasNext()) {
				InfoShiftProfessorship infoTeacherShiftPercentage = (InfoShiftProfessorship) iterator.next();
				teacherShiftPercentage = Cloner.copyInfoShiftProfessorship2IShiftProfessorship(infoTeacherShiftPercentage);

				teacherShiftPercentage = teacherShiftPercentageDAO.readByUnique(teacherShiftPercentage);
				assertNull("Not deleted!!", teacherShiftPercentage);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Getting credits from database!!");
		}
	}

	private void testProfessorShip(Integer professorShipId, Integer expectedSizeOfAssociatedTeacherPercentage) {
		IProfessorship professorship = null;
		PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();

		pb.clearCache();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

			professorship = new Professorship();
			professorship.setIdInternal(professorShipId);
			professorship = (IProfessorship) professorshipDAO.readByOId(professorship, false);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Getting professorship id=" + professorShipId);
		}

		assertEquals(
			"Size is not the same on professorship!",
			expectedSizeOfAssociatedTeacherPercentage.intValue(),
			professorship.getAssociatedShiftProfessorship().size());

	}

	private void testPercentage(Integer shiftId, Double expectedPercentage) {

		ITurno shift = new Turno();
		shift.setIdInternal(shiftId);
		ISuportePersistente sp;

		PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();

		pb.clearCache();

		try {
			sp = SuportePersistenteOJB.getInstance();
			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
			shift = (ITurno) shiftDAO.readByOId(shift, false);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading shift! id=" + shiftId);
		}
		List associatedTeacherProfessorShipPercentage = shift.getAssociatedShiftProfessorship();

		Iterator iterator = associatedTeacherProfessorShipPercentage.iterator();
		double realPercentage = 0;
		while (iterator.hasNext()) {
			IShiftProfessorship teacherShiftPercentage = (IShiftProfessorship) iterator.next();
			realPercentage += teacherShiftPercentage.getPercentage().doubleValue();
		}
		assertEquals("Percentage test! Shift with id=" + shiftId, expectedPercentage, new Double(realPercentage));
	}

	private List getInfoTeacherShiftPercentageList(Object[][] shiftPercentageArray) {
		List infoTeacherShiftPercentageList = new ArrayList();
		for (int i = 0; i < shiftPercentageArray.length; i++) {
			Integer shiftId = (Integer) shiftPercentageArray[i][0];
			Double percentage = (Double) shiftPercentageArray[i][1];

			//Shift 1 está cheio
			InfoShift infoShift = new InfoShift();
			infoShift.setIdInternal(shiftId);

			//Elemento da lista
			InfoShiftProfessorship infoTeacherShiftPercentage = new InfoShiftProfessorship();
			infoTeacherShiftPercentage.setInfoShift(infoShift);
			infoTeacherShiftPercentage.setInfoProfessorship(null);
			infoTeacherShiftPercentage.setPercentage(percentage);

			infoTeacherShiftPercentageList.add(infoTeacherShiftPercentage);
		}
		return infoTeacherShiftPercentageList;
	}

	private InfoTeacher getInfoTeacher(Integer teacherInternalCode) {
		ISuportePersistente sp;
		ITeacher teacher = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			teacher = new Teacher();
			teacher.setIdInternal(teacherInternalCode);
			teacher = (ITeacher) teacherDAO.readByOId(teacher, false);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading teacher! id=" + teacherInternalCode);
		}

		return Cloner.copyITeacher2InfoTeacher(teacher);
	}

	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);

		return userView;
	}

	public InfoExecutionCourse getInfoExecutionCourse(Integer id) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setIdInternal(id);
		return infoExecutionCourse;
	}
}