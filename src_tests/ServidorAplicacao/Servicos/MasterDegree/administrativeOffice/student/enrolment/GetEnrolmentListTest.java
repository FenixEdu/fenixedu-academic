
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.student.enrolment;

/**
 *
 * @author Nuno Nunes & Joana Mota 
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servicos.TestCaseReadServicesIntranet;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;
import Util.TipoCurso;

public class GetEnrolmentListTest extends TestCaseReadServicesIntranet {
  
  public GetEnrolmentListTest(java.lang.String testName) {
	super(testName);
  }
    
  public static void main(java.lang.String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(GetEnrolmentListTest.class);
        
	return suite;
  }
    
  protected void setUp() {
	super.setUp();
  }
    
  protected void tearDown() {
	super.tearDown();
  }

  protected String getNameOfServiceToBeTested() {
	  return "GetEnrolmentList";
  }

  protected int getNumberOfItemsToRetrieve(){
	  return 3;
  }
  protected Object getObjectToCompare(){
  	
	  return null;
  }
	
  protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	ISuportePersistente sp = null;
	InfoStudentCurricularPlan infoStudentCurricularPlan = null;

	try {
		sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();
		IStudentCurricularPlan iStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveStudentAndSpecializationCurricularPlan(
														new Integer(41329), new TipoCurso(TipoCurso.MESTRADO_STRING), new Specialization(Specialization.MESTRADO_STRING));
		assertNotNull(iStudentCurricularPlan);	
		
		infoStudentCurricularPlan = Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(iStudentCurricularPlan);	
		sp.confirmarTransaccao();
	
	} catch(Exception e) {
		fail("Error !");  		
	}

	Object[] args = { infoStudentCurricularPlan };

	return args;
 
  }
	

  protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	return null;
  }
}