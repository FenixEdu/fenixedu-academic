/*
 * Created on 20/Out/2003
 * 
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import DataBeans.InfoItem;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteItems;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio
 * @author Nuno Ochoa
 *
 */
public class ReadSiteItemTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * 
	 */
	public ReadSiteItemTest(String name) {
		super(name);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(27);
		Integer infoSiteCode = new Integer(4);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoSiteItems bodyComponent = new InfoSiteItems();
		Object obj1 = new Integer(1);
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
		return "etc/datasets/servicos/teacher/testReadSiteItemDataSet.xml";
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

	public void testReadSiteItems() {
		TeacherAdministrationSiteView result = null;
		IItem item = new Item(new Integer(1));

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentItem persistentItem = sp.getIPersistentItem();
			item = (IItem) persistentItem.readByOId(item, false);
			sp.confirmarTransaccao();

			result =
				(TeacherAdministrationSiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSiteItems bodyComponent = (InfoSiteItems) result.getComponent();
			InfoItem infoItem = bodyComponent.getItem();
			List items = bodyComponent.getItems();

			IItem iItem = Cloner.copyInfoItem2IItem(infoItem);

			assertTrue(item.equals(iItem));

			assertEquals(items.size(), 2);

			compareDataSet("etc/datasets/servicos/teacher/testExpectedReadSiteItemDataSet.xml");

			System.out.println(
				"testDeleteExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteExistingItem");
		}
	}

	public void testReadNonSiteItem() {
		TeacherAdministrationSiteView result = null;

		Integer infoExecutionCourseCode = new Integer(27);
		Integer infoSiteCode = new Integer(4);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoSiteItems bodyComponent = new InfoSiteItems();
		Object obj1 = new Integer(2);
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				obj1,
				obj2 };

		try {

			result =
				(TeacherAdministrationSiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);

			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteExistingItem");
		} catch (Exception ex) {
			System.out.println(
				"testDeleteExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		}
	}

	public void testReadNonExistingItem() {

		Integer infoExecutionCourseCode = new Integer(27);
		Integer infoSiteCode = new Integer(4);
		InfoSiteCommon commonComponent = new InfoSiteCommon();
		InfoSiteItems bodyComponent = new InfoSiteItems();
		Object obj1 = new Integer(100);
		Object obj2 = null;

		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				bodyComponent,
				infoSiteCode,
				obj1,
				obj2 };

		try {

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				args);

			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteExistingItem");
		} catch (NullPointerException ex) {
			System.out.println(
				"testDeleteExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
		}
	}
}
