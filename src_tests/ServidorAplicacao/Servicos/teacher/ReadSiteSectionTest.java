/*
 * Created on 20/Out/2003
 * 
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoSection;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSection;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.Section;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio
 * @author Nuno Ochoa
 *
 */
public class ReadSiteSectionTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * 
	 */
	public ReadSiteSectionTest(String name) {
		super(name);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(27);
		Integer infoSiteCode = new Integer(4);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoSiteSection bodyComponent = new InfoSiteSection();
		Object obj1 = new Integer(6);
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
		return null;
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

	public void testReadSiteSection() {
		TeacherAdministrationSiteView result = null;
		ISection section = new Section(new Integer(6));

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			section = (ISection) persistentSection.readByOId(section, false);
			sp.confirmarTransaccao();
			
			result =
				(TeacherAdministrationSiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSiteSection bodyComponent =
				(InfoSiteSection) result.getComponent();
			InfoSection infoSection = bodyComponent.getSection();
			ISection iSection = Cloner.copyInfoSection2ISection(infoSection);
			
			assertTrue(section.equals(iSection));

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
