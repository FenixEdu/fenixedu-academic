/*
 * Created on 21/Mar/2003 by jpvl
 *
 */
package ServidorApresentacao.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCaseActionPresentationPersonPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EstadoCivil;
import Util.TipoDocumentoIdentificacao;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public class ChangePersonalInfoDispatchActionChangeMethodTest
	extends TestCaseActionPresentationPersonPortal {
	/**
	 * Main method 
	 * @param args
	 */
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * 
	 * @return Test to be done
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(ChangePersonalInfoDispatchActionChangeMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ChangePersonalInfoDispatchActionChangeMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly() 
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap requestParameters = new HashMap();
		requestParameters.put("method","change");
		requestParameters.put("birthYear","1979");
		requestParameters.put("birthMonth","5");
		requestParameters.put("birthDay","22");
		
		requestParameters.put("idIssueDateYear","1979");
		requestParameters.put("idIssueDateMonth","5");
		requestParameters.put("idIssueDateDay","22");
		
		
		requestParameters.put("idExpirationDateYear","2004");
		requestParameters.put("idExpirationDateMonth","5");
		requestParameters.put("idExpirationDateDay","22");

		requestParameters.put("nationality","Portugesa");
		requestParameters.put("identificationDocumentType",TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING);
		requestParameters.put("identificationDocumentNumber","999999");
		requestParameters.put("identificationDocumentIssuePlace","Lisbon");
		requestParameters.put("name","Nuno Nunes");
		requestParameters.put("sex","Masculino");
		requestParameters.put("MaritalStatus", EstadoCivil.CASADO_STRING);
		requestParameters.put("fatherName","Manuel Jose");
		requestParameters.put("motherName","Maria Rosa");
		requestParameters.put("birthPlaceParish","Lisbon");
		requestParameters.put("birthPlaceMunicipality","Lisbon");
		requestParameters.put("birthPlaceDistrict","Lisbon");
		requestParameters.put("address","Nuno's Street");
		requestParameters.put("place","Nuno's Place");
		requestParameters.put("postCode","2222-222");
		requestParameters.put("addressParish","Address");
		requestParameters.put("addressMunicipality","Address");
		requestParameters.put("addressDistrict","Address");
		requestParameters.put("telephone","222222222");
		requestParameters.put("mobilePhone","999999999");
		requestParameters.put("email","email@email.com");
		requestParameters.put("webSite","www.Fenix.com");
		requestParameters.put("contributorNumber","132456");
		requestParameters.put("occupation","None");
		requestParameters.put("username","nmsn");
		requestParameters.put("areaOfAreaCode","none");

		return requestParameters;
	}

	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		
		List sessionAttributes = new ArrayList();
		sessionAttributes.add(SessionConstants.PERSONAL_INFO_KEY);
		attributes.put(new Integer(ScopeConstants.SESSION),sessionAttributes);
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected String getRequestPathInfoNameAction() {
		return "/changePersonalInfoDispatchAction";
	}

	protected String getSuccessfulForward() {
		return "Success";
	}


}
