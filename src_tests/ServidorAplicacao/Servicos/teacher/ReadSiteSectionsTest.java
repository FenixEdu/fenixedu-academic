/*
 * Created on 20/Out/2003
 * 
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Luis Egidio
 * @author Nuno Ochoa
 *
 */
public class ReadSiteSectionsTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * 
	 */
	public ReadSiteSectionsTest(String name) {
		super(name);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(27);
		Integer infoSiteCode = new Integer(4);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		ISiteComponent bodyComponent = null;
		Object obj1 = null;
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				obj1,
				obj2 };
		return args;
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;

	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testReadSiteSectionsDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "TeacherAdministrationSiteComponentService";
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;

	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
	}

	public void testReadSiteSections() {
		TeacherAdministrationSiteView result = null;
		try {
			result =
				(TeacherAdministrationSiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSiteCommon commonComponent =
				(InfoSiteCommon) result.getCommonComponent();
			List sections = commonComponent.getSections();
			assertEquals(sections.size(), 5);
			
			compareDataSet("etc/datasets/servicos/teacher/testExpectedReadSiteSectionsDataSet.xml");

			System.out.println(
				"testDeleteExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteExistingItem");
		}
	}

}
