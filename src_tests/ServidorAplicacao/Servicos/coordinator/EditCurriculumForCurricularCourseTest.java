package ServidorAplicacao.Servicos.coordinator;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 21/Nov/2003
 *  
 */
public class EditCurriculumForCurricularCourseTest extends ServiceTestCase
{
	public EditCurriculumForCurricularCourseTest(String testName)
	{
		super(testName);
	}

	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested()
	{
		return "EditCurriculumForCurricularCourse";
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets_templates/servicos/coordinator/testDataSetCurriculum.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser()
	{
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getSecondAuthenticatedAndAuthorizedUser()
	{
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser()
	{
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	public InfoCurriculum getCurriculumForm(Integer curriculumCode)
	{
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curriculumCode);

		infoCurriculum.setGeneralObjectives("objectivos gerais em portugues anterior modificado");
		infoCurriculum.setGeneralObjectivesEn("objectivos gerais em ingles anterior modificado");
		infoCurriculum.setOperacionalObjectives(
			"objectivos operacionais em portugues anterior modificado");
		infoCurriculum.setOperacionalObjectivesEn(
			"objectivos operacionais em ingles anterior modificado");
		infoCurriculum.setProgram("programa em portugues anterior modificado");
		infoCurriculum.setProgramEn("programa em ingles anterior modificado");

		return infoCurriculum;
	}

	public InfoCurriculum getNewCurriculumForm(Integer curriculumCode)
	{
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curriculumCode);

		infoCurriculum.setGeneralObjectives("objectivos gerais em portugues");
		infoCurriculum.setGeneralObjectivesEn("objectivos gerais em ingles");
		infoCurriculum.setOperacionalObjectives("objectivos operacionais em portugues");
		infoCurriculum.setOperacionalObjectivesEn("objectivos operacionais em ingles");
		infoCurriculum.setProgram("programa");
		infoCurriculum.setProgramEn("programa em ingles");

		return infoCurriculum;
	}

	public void testSuccessfull()
	{
		try
		{
			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();

			//Service Arguments
			Integer infoExecutionDegreeCode = new Integer(30);
			Integer infoCurriculumCode = new Integer(3);
			Integer infoCurricularCourseCode = new Integer(54);
			InfoCurriculum infoCurriculum = getCurriculumForm(infoCurriculumCode);

			Object[] args =
				{
					infoExecutionDegreeCode,
					infoCurriculumCode,
					infoCurricularCourseCode,
					infoCurriculum,
					argsUser[0] };

			IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

			//Service
			ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

			//Read the change in curriculum
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

			sp.iniciarTransaccao();
			ICurriculum curriculumAck = new Curriculum();
			curriculumAck.setIdInternal(infoCurriculumCode);

			curriculumAck = (ICurriculum) persistentCurriculum.readByOId(curriculumAck, false);
			sp.confirmarTransaccao();

			assertEquals(infoCurriculum.getGeneralObjectives(), curriculumAck.getGeneralObjectives());
			assertEquals(
				infoCurriculum.getGeneralObjectivesEn(),
				curriculumAck.getGeneralObjectivesEn());
			assertEquals(
				infoCurriculum.getOperacionalObjectives(),
				curriculumAck.getOperacionalObjectives());
			assertEquals(
				infoCurriculum.getOperacionalObjectivesEn(),
				curriculumAck.getOperacionalObjectivesEn());
			assertEquals(infoCurriculum.getProgram(), curriculumAck.getProgram());
			assertEquals(infoCurriculum.getProgramEn(), curriculumAck.getProgramEn());
			assertEquals(argsUser[0], curriculumAck.getPersonWhoAltered().getUsername());
			assertNotNull(curriculumAck.getCurricularCourse());

			System.out.println(
				"EditCurriculumForCurricularCourseTest was SUCCESSFULY runned by service: testSuccessfull");

		} catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("Reading a curriculum " + e);
		} catch (Exception e)
		{
			e.printStackTrace();
			fail("Reading a curriculum " + e);
		}
	}

	public void testNewCurriculum()
	{
		try
		{
			//Service Argument
			Integer infoExecutionDegreeCode = new Integer(30);
			Integer infoCurriculumCode = new Integer(0);
			Integer infoCurricularCourseCode = new Integer(54);
			InfoCurriculum infoCurriculum = getNewCurriculumForm(infoCurriculumCode);

			String[] argsUser = getAuthenticatedAndAuthorizedUser();

			Object[] args =
				{
					infoExecutionDegreeCode,
					infoCurriculumCode,
					infoCurricularCourseCode,
					infoCurriculum,
					argsUser[0] };

			//Valid user
			IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

			//Service
			ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

			//Read the change in curriculum
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

			sp.iniciarTransaccao();
			ICurricularCourse curricularCourseAck = new CurricularCourse();
			curricularCourseAck.setIdInternal(infoCurricularCourseCode);

			curricularCourseAck =
				(ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourseAck, false);
			ICurriculum curriculumAck =
				persistentCurriculum.readCurriculumByCurricularCourse(curricularCourseAck);
			sp.confirmarTransaccao();

			assertEquals(infoCurriculum.getGeneralObjectives(), curriculumAck.getGeneralObjectives());
			assertEquals(
				infoCurriculum.getGeneralObjectivesEn(),
				curriculumAck.getGeneralObjectivesEn());
			assertEquals(
				infoCurriculum.getOperacionalObjectives(),
				curriculumAck.getOperacionalObjectives());
			assertEquals(
				infoCurriculum.getOperacionalObjectivesEn(),
				curriculumAck.getOperacionalObjectivesEn());
			assertEquals(infoCurriculum.getProgram(), curriculumAck.getProgram());
			assertEquals(infoCurriculum.getProgramEn(), curriculumAck.getProgramEn());
			assertEquals(argsUser[0], curriculumAck.getPersonWhoAltered().getUsername());
			assertNotNull(curriculumAck.getCurricularCourse());

			System.out.println(
				"EditCurriculumForCurricularCourseTest was SUCCESSFULY runned by service: testNewCurriculum");

		} catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("Reading a curriculum " + e);
		} catch (Exception e)
		{
			e.printStackTrace();
			fail("Reading a curriculum " + e);
		}
	}
}
