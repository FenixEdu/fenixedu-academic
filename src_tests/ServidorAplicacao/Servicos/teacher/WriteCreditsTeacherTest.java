package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import DataBeans.InfoRole;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *
 */
public class WriteCreditsTeacherTest extends TestCaseServices {
	/**
	 * @param testName
	 */
	public WriteCreditsTeacherTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "WriteCreditsTeacher";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForTeacherCredits.xml";
	}

	public static void main(java.lang.String[] args){
				TestRunner.run(suite());
	}
	
	public static Test suite(){
		TestSuite testSuite = new TestSuite(WriteCreditsTeacherTest.class);
		return testSuite;
	}
		
	public void testDelete() {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Teacher		
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(new Integer(2));

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			sp.iniciarTransaccao();
			teacher = (ITeacher) teacherDAO.readByOId(teacher, false);

			sp.confirmarTransaccao();

			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

			Integer tfcStudentsNumber = new Integer(0);

			Object[] args = { infoTeacher, tfcStudentsNumber };

			Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(authorizedUserView(), getNameOfServiceToBeTested(), args);

			if (!result.booleanValue()) {
				fail("can't execute service");
			}

			testDeleted(sp, teacher);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading database!");
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	private void testDeleted(ISuportePersistente sp, ITeacher teacher) {
		IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
		IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
		ICredits credits = new Credits();

		try {
			sp.iniciarTransaccao();

			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			sp.confirmarTransaccao();

			ITeacher teacher2 = new Teacher();
			teacher2.setIdInternal(teacher.getIdInternal());
			credits.setTeacher(teacher2);
			credits.setExecutionPeriod(executionPeriod);

			sp.iniciarTransaccao();
			credits = (ICredits) creditsTeacherDAO.readDomainObjectByCriteria(credits);
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Getting credits from database!!");
		}

		assertNull("Not deleted!", credits);
	}

	public void testWrite() {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Teacher		
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(new Integer(2));

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			sp.iniciarTransaccao();
			teacher = (ITeacher) teacherDAO.readByOId(teacher, false);

			sp.confirmarTransaccao();

			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

			//Service: insert
			Integer tfcStudentsNumber = new Integer(4);

			Object[] args = { infoTeacher, tfcStudentsNumber };

			Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(authorizedUserView(), getNameOfServiceToBeTested(), args);

			if (!result.booleanValue()) {
				fail("can't execute service");
			}

			testCredits(sp, teacher, tfcStudentsNumber);

			//Service: change
			tfcStudentsNumber = new Integer(6);

			Object[] args2 = { infoTeacher, tfcStudentsNumber };

			result = (Boolean) ServiceManagerServiceFactory.executeService(authorizedUserView(), getNameOfServiceToBeTested(), args2);

			if (!result.booleanValue()) {
				fail("can't execute service");
			}

			testCredits(sp, teacher, tfcStudentsNumber);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading database!");
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public void testCredits(ISuportePersistente sp, ITeacher teacher, Integer tfcStudentsNumber) {
		IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
		IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

		ICredits credits = new Credits();

		try {
			sp.iniciarTransaccao();

			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			sp.confirmarTransaccao();

			ITeacher teacher2 = new Teacher();
			teacher2.setIdInternal(teacher.getIdInternal());
			credits.setTeacher(teacher2);
			credits.setExecutionPeriod(executionPeriod);

			sp.iniciarTransaccao();
			credits = (ICredits) creditsTeacherDAO.readDomainObjectByCriteria(credits);
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Getting credits from database!!");
		}

		assertEquals("TfcStudentsnumber", tfcStudentsNumber, credits.getTfcStudentsNumber());
	}

	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);

		return userView;
	}
}
