package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoItem;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Fernanda Quitério
 * 
 */
public class EditItemTest extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditItemTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditItem";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		InfoItem  newInfoItem = new InfoItem();
		newInfoItem.setInformation("newInformation");
		newInfoItem.setName("NewItemName");
		newInfoItem.setItemOrder(new Integer(1));
		newInfoItem.setUrgent(new Boolean(false));

		Object[] args = { new Integer(24), new Integer(2), newInfoItem };
		return args;
	}

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}
}
