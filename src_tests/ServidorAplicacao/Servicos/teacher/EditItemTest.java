package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoItem;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class EditItemTest extends ItemBelongsExecutionCourseTest {

	/**
	 * @param testName
	 */
	public EditItemTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "EditItem";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testEditItemDataSet.xml";
	}
	
	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		InfoItem infoItem = new InfoItem();
		infoItem.setInformation("informacao");
		infoItem.setName("nome");
		infoItem.setItemOrder(new Integer(0));
		infoItem.setUrgent(new Boolean(false));

		Object[] args = { new Integer(27), new Integer(1), infoItem };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getTestItemSuccessfullArguments() {
		InfoItem infoItem = new InfoItem();
		infoItem.setInformation("informacao");
		infoItem.setName("nome");
		infoItem.setItemOrder(new Integer(0));
		infoItem.setUrgent(new Boolean(false));

		Object[] args = { new Integer(27), new Integer(1), infoItem };
		return args;

	}

	protected Object[] getTestItemUnsuccessfullArguments() {
		InfoItem infoItem = new InfoItem();
		infoItem.setInformation("informacao");
		infoItem.setName("nome");
		infoItem.setItemOrder(new Integer(0));
		infoItem.setUrgent(new Boolean(false));

		Object[] args = { new Integer(27), new Integer(2), infoItem };
		return args;
	}

	public void testEditExistingItem() {
		Object[] args = getTestItemSuccessfullArguments();
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);

			compareDataSet("etc/datasets/servicos/teacher/testExpectedEditItemDataSet.xml");
			System.out.println(
				"testEditExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testEditExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditExistingItem");
		}
	}

	public void testEditNonExistingItem() {
		InfoItem infoItem = new InfoItem();
		infoItem.setInformation("informacao");
		infoItem.setName("nome");
		infoItem.setItemOrder(new Integer(0));
		infoItem.setUrgent(new Boolean(false));

		Object[] args = { new Integer(27), new Integer(100), infoItem };
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testEditNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditNonExistingItem");

		} catch (NotAuthorizedException e) {

// O Teste falha pq ele n lança esta excepção, lança NullPointerException...

			System.out.println(
				"testEditNonExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testEditNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditNonExistingItem");
		}
	}

}
