
package ServidorApresentacao.Action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationMDAdministrativeOffice;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.SituationName;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class ListCandidateDispatchActionChangeMethodTest
	extends TestCasePresentationMDAdministrativeOffice{
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
		TestSuite suite = new TestSuite(ListCandidateDispatchActionChangeMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ListCandidateDispatchActionChangeMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		HashMap sessionParameters = new HashMap();
		sessionParameters.put(SessionConstants.MASTER_DEGREE_CANDIDATE, this.getCandidate());

		// These parameters will be put at null because the aren't used in the Action, they are only cleaned
		sessionParameters.put(SessionConstants.NATIONALITY_LIST_KEY, null);
		sessionParameters.put(SessionConstants.MARITAL_STATUS_LIST_KEY, null);
		sessionParameters.put(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY, null);
		sessionParameters.put(SessionConstants.SEX_LIST_KEY, null);
		sessionParameters.put(SessionConstants.MONTH_DAYS_KEY, null);
		sessionParameters.put(SessionConstants.MONTH_LIST_KEY, null);
		sessionParameters.put(SessionConstants.YEARS_KEY, null);
		sessionParameters.put(SessionConstants.EXPIRATION_YEARS_KEY, null);
		sessionParameters.put(SessionConstants.CANDIDATE_SITUATION_LIST, null);

		return sessionParameters;

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
		requestParameters.put("birthDay","10");
		requestParameters.put("birthMonth","4");
		requestParameters.put("birthYear","2000");
		
		requestParameters.put("idIssueDateDay","10");
		requestParameters.put("idIssueDateMonth","4");
		requestParameters.put("idIssueDateYear","2000");

		requestParameters.put("idExpirationDateDay","10");
		requestParameters.put("idExpirationDateMonth","4");
		requestParameters.put("idExpirationDateYear","2000");
		

		requestParameters.put("nationality","Portuguesa");

		requestParameters.put("identificationDocumentType",TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING);
		requestParameters.put("identificationDocumentNumber","9999");
		requestParameters.put("identificationDocumentIssuePlace","Lisbon");
		
		requestParameters.put("sex",Sexo.MASCULINO_STRING);
			

		requestParameters.put("maritalStatus",EstadoCivil.CASADO_STRING);

		requestParameters.put("name","Nuno Nunes");

		requestParameters.put("fatherName","Manuel");
		requestParameters.put("motherName","Maria");

		requestParameters.put("birthPlaceParish","Lisbon");
		requestParameters.put("birthPlaceMunicipality","Lisbon");
		requestParameters.put("birthPlaceDistrict","Lisbon");
				
		requestParameters.put("address","Lisbon");
		requestParameters.put("place","Lisbon");
		requestParameters.put("postCode","1234-123");

		requestParameters.put("addressParish","Lisbon");
		requestParameters.put("addressMunicipality","Lisbon");
		requestParameters.put("addressDistrict","Lisbon");

		requestParameters.put("telephone","123456789");
		requestParameters.put("mobilePhone","123456789");

		requestParameters.put("email","nmsn@rnl.ist.utl.pt");
		requestParameters.put("webSite","www.fenix.com");

		requestParameters.put("contributorNumber","132456");
		requestParameters.put("occupation","Student");
		requestParameters.put("username","nmsn");
		requestParameters.put("areaOfAreaCode","Lisbon");

		requestParameters.put("majorDegree","LEIC");
		requestParameters.put("majorDegreeYear","2000");
		requestParameters.put("majorDegreeSchool","IST");

		requestParameters.put("average","10.01");
			
		requestParameters.put("situation", SituationName.ADMITIDO_STRING);
		requestParameters.put("situationRemarks","None");
			
		return requestParameters;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		List requestAttributes = new ArrayList();
		requestAttributes.add(SessionConstants.MASTER_DEGREE_CANDIDATE);
		attributes.put(new Integer(ScopeConstants.SESSION),requestAttributes);
		return attributes;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/editCandidate";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "ChangeSuccess";
	}

	private InfoMasterDegreeCandidate getCandidate(){
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
		SuportePersistenteOJB sp = null;
		
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);
		
		UserView userView = new UserView("nmsn", roles);
		
		ICursoExecucao executionDegree = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			
			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
							"MEEC", "plano2", executionYear);
			assertNotNull(executionDegree);
			sp.confirmarTransaccao();
		} catch(Exception e) {
			fail("Error reading from DataBase ! " + e);
			
		}

		GestorServicos serviceManager = GestorServicos.manager();
		
		InfoExecutionDegree infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
		Object args[] = {infoExecutionDegree, new Integer(1), new Specialization(Specialization.MESTRADO)};		
		
		try {
			infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "ReadMasterDegreeCandidate", args);
			assertNotNull(infoMasterDegreeCandidate);
		} catch (Exception e) {
			fail("Error Reading Candidate !!" + e);
		}
		
		return infoMasterDegreeCandidate;
		
	}

}
