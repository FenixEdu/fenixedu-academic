/*
 * Created on 20/Out/2003
 * 
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoItem;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteItems;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class ReadSiteItemTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public ReadSiteItemTest(String name) {
		super(name);
	}

	protected String getNameOfServiceToBeTested() {
		return "TeacherAdministrationSiteComponentService";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testReadSiteItemDataSet.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
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

	protected String getApplication() {
		return Autenticacao.EXTRANET;
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
				(TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSiteItems bodyComponent = (InfoSiteItems) result.getComponent();
			InfoItem infoItem = bodyComponent.getItem();
			List items = bodyComponent.getItems();

			IItem iItem = Cloner.copyInfoItem2IItem(infoItem);

			assertTrue(item.equals(iItem));

			assertEquals(items.size(), 2);

			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedReadSiteItemDataSet.xml");

			System.out.println(
				"testReadSiteItems was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadSiteItems was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadSiteItems");
		}
	}

	public void testReadNonSiteItem() {

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
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

			System.out.println(
				"testReadNonSiteItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadNonSiteItem");
		} catch (Exception ex) {
			System.out.println(
				"testReadNonSiteItem was SUCCESSFULY runned by class: "
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

			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

			System.out.println(
				"testReadNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadNonExistingItem");
		} catch (FenixServiceException ex) {
			System.out.println(
				"testReadNonExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadNonExistingItem");
		}
	}
}
