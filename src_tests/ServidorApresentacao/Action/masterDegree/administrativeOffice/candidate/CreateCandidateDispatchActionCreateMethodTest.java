
package ServidorApresentacao.Action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationMDAdministrativeOffice;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class  CreateCandidateDispatchActionCreateMethodTest
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
		TestSuite suite = new TestSuite(CreateCandidateDispatchActionCreateMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public CreateCandidateDispatchActionCreateMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		HashMap sessionParameters = new HashMap();
		List degrees = new ArrayList();
		degrees.add(this.getDegree());
		sessionParameters.put(SessionConstants.DEGREE_LIST, degrees);
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
		requestParameters.put("method","create");
		requestParameters.put("specialization", Specialization.MESTRADO_STRING);
		requestParameters.put("degree", "Mestrado em Engenharia Electrotecnica e de Computadores");
		requestParameters.put("name", "Nuno Nunes");
		requestParameters.put("identificationDocumentNumber", "9999");
		requestParameters.put("identificationDocumentType", TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING);
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
		requestAttributes.add(SessionConstants.NEW_MASTER_DEGREE_CANDIDATE);
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
		return "/createCandidateDispatchAction";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "CreateSuccess";
	}

	private InfoExecutionDegree getDegree(){
		SuportePersistenteOJB sp = null;
		ICursoExecucao executionDegree = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			
			IExecutionYear executionYear = null;
			
			executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
							"MEEC", "plano2", executionYear);
			assertNotNull(executionDegree);
			
			
			sp.confirmarTransaccao();
			
		} catch(Exception e) {
			fail("Error reading from DataBase ! " + e);
			
		}
		
		return Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
		
	}

}
