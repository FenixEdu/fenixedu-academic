package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

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
		return "WriteCreditsTeacherTest";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForTeacherCredits.xml";
	}

	public void test() {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Teacher		
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(new Integer(2));

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			teacher = (ITeacher) teacherDAO.readByOId(teacher);

			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

			//Service: insert
			GestorServicos serviceManager = GestorServicos.manager();
			Integer tfcStudentsNumber = new Integer(4);

			Object[] args = { infoTeacher, tfcStudentsNumber };

			Boolean result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);

			if (!result.booleanValue()) {
				fail("can't execute service");
			}

			testCredits(infoTeacher, tfcStudentsNumber);


			//Service: change
			serviceManager = GestorServicos.manager();
			tfcStudentsNumber = new Integer(6);

			Object[] args2 = { infoTeacher, tfcStudentsNumber };

			result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args2);

			if (!result.booleanValue()) {
				fail("can't execute service");
			}

			testCredits(infoTeacher, tfcStudentsNumber);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading database!");
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public void testCredits(InfoTeacher infoTeacher, Integer tfcStudentsNumber) {
		try {
			//Service
			GestorServicos serviceManager = GestorServicos.manager();

			Object[] args = { infoTeacher };

			InfoCredits credits = (InfoCredits) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);

			assertEquals("TfcStudentsnumber", tfcStudentsNumber, credits.getTfcStudentsNumber());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
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
