
package ServidorApresentacao.Action.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationMDAdministrativeOffice;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public class ChooseDeclarationChooseActionTest
	extends TestCasePresentationMDAdministrativeOffice {
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
		TestSuite suite = new TestSuite(ChooseDeclarationChooseActionTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ChooseDeclarationChooseActionTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
				
//		InfoPerson infoPerson = new InfoPerson();
//		infoPerson.setNumeroDocumentoIdentificacao("7712345");
//		infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
//		infoPerson.setUsername("jccm");
//		
//		InfoStudent infoStudent = new InfoStudent();
//		infoStudent.setNumber(new Integer(46865));
//		infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
//		infoStudent.setInfoPerson(infoPerson);
//		
//		InfoDegree infoDegree = new InfoDegree();
//		infoDegree.setTipoCurso(new TipoCurso(TipoCurso.MESTRADO));
//		infoDegree.setNome("Engenharia Electrotecnica e de Computadores");
//		
//		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
//		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
//		
//		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
//		infoStudentCurricularPlan.setInfoStudent(infoStudent);
//		infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
//	    infoStudentCurricularPlan.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
//				
//		Locale locale = new Locale("pt", "PT");
//		Date date = new Date();
//		String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
//						
//		HashMap parameters = new HashMap();
//		
//		parameters.put(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());
//		parameters.put(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
//		parameters.put(SessionConstants.DATE, formatedDate);
//		return parameters;
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
		HashMap parameters = new HashMap();
		parameters.put("requesterNumber","46865");
		parameters.put("graduationType", "Especialização");
		parameters.put("destination", "Fins militares");
		parameters.put("method","choose");
		return parameters;
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
		requestAttributes.add(SessionConstants.DOCUMENT_REASON_LIST);
		requestAttributes.add(SessionConstants.DEGREE_TYPE);
		requestAttributes.add(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
		requestAttributes.add(SessionConstants.DATE);
		requestAttributes.add(SessionConstants.INFO_EXECUTION_YEAR);
		attributes.put(new Integer(ScopeConstants.SESSION), requestAttributes);
		
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
		return "/chooseDeclarationInfoAction";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "ChooseSuccess";
	}


}
