package ServidorPersistente.OJB;

import junit.framework.TestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import Tools.dbaccess;

public class TestCaseOJB extends TestCase {
  protected ISuportePersistente persistentSupport = null;
  protected IPessoaPersistente persistentPerson = null;

  protected ICursoExecucaoPersistente persistentExecutionDegree = null;
  protected ICursoPersistente persistentDegree = null;

  protected IDisciplinaExecucaoPersistente persistentExecutionCourse = null;

  protected IAulaPersistente persistentLesson = null;
  protected ISalaPersistente persistentRoom = null;
  protected ITurmaPersistente persistentClass = null;
  protected ITurnoPersistente persistentShift = null;
  protected IFrequentaPersistente persistentAttend = null;
  protected IPersistentEnrolment persistentEnrollment = null;  
  protected ITurmaTurnoPersistente persistentClassShift = null;
  protected ITurnoAlunoPersistente persistentStudentShift = null;  
  protected ITurnoAulaPersistente persistentShiftLesson = null;

  protected IPersistentStudent persistentStudent = null;

  protected IDepartamentoPersistente persistentDepartment = null;
  protected IDisciplinaDepartamentoPersistente persistentDepartmentCourse = null;

  protected IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan = null;
  protected IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null; 
  protected IPersistentCountry persistentCountry = null;
  protected IPersistentCurricularCourse persistantCurricularCourse = null;

  protected IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
  protected IPersistentCandidateSituation persistentCandidateSituation = null;
  protected IPersistentExecutionYear persistentExecutionYear = null;
  protected IPersistentExecutionPeriod persistentExecutionPeriod = null;

  private dbaccess dbAcessPoint = null;
    
  public TestCaseOJB(String testName) {
    super(testName);
  }
  /*
    public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
    TestSuite suite = new TestSuite(TestCaseOJB.class);
        
    return suite;
    }*/

  protected void setUp() {
  	// The following code backs up the contents of the database
  	// and loads the database with the data set required to run
  	// the test cases.
  	try {
  		dbAcessPoint = new dbaccess();
  		dbAcessPoint.openConnection();
  		dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
  		dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Setup failed: " + ex);
  	}
  	
    ligarSuportePersistente();
  }

  protected void tearDown() {
  	try {
//		persistentSupport.iniciarTransaccao();
//		persistentSupport.confirmarTransaccao();
  		dbAcessPoint.openConnection();
  		dbAcessPoint.loadDataBase("etc/testBackup.xml");
  		//dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Tear down failed: " +ex);
  	}
  }
            
  protected void ligarSuportePersistente() {
    try {
      persistentSupport = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    persistentPerson = persistentSupport.getIPessoaPersistente();

    persistentLesson = persistentSupport.getIAulaPersistente();
    persistentRoom = persistentSupport.getISalaPersistente();
    persistentClass = persistentSupport.getITurmaPersistente();
    persistentShift = persistentSupport.getITurnoPersistente();
    
    persistentAttend = persistentSupport.getIFrequentaPersistente();
    persistentEnrollment = persistentSupport.getIInscricaoPersistente();    
    persistentClassShift = persistentSupport.getITurmaTurnoPersistente();
    persistentStudentShift = persistentSupport.getITurnoAlunoPersistente();    
    persistentShiftLesson = persistentSupport.getITurnoAulaPersistente();        

    persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
    persistentDegree = persistentSupport.getICursoPersistente();

    persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
    
    persistentStudent = persistentSupport.getIPersistentStudent();
    
    persistentDepartment = persistentSupport.getIDepartamentoPersistente();
    persistentDepartmentCourse = persistentSupport.getIDisciplinaDepartamentoPersistente();
    persistentDegreeCurricularPlan = persistentSupport.getIPlanoCurricularCursoPersistente();
    persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
    persistentCountry = persistentSupport.getIPersistentCountry();
    persistantCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
    persistentMasterDegreeCandidate = persistentSupport.getIPersistentMasterDegreeCandidate();
    persistentCandidateSituation = persistentSupport.getIPersistentCandidateSituation();
	persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
	persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
 
  }
    
}
