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
import ServidorPersistente.IItemPersistente;
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
import ServidorPersistente.ISeccaoPersistente;
import ServidorPersistente.ISitioPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import Tools.dbaccess;

public class TestCaseOJB extends TestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected ISitioPersistente _sitioPersistente = null;
  protected ISeccaoPersistente _seccaoPersistente = null;
  protected IItemPersistente _itemPersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;

  protected ICursoExecucaoPersistente cursoExecucaoPersistente = null;
  protected ICursoPersistente cursoPersistente = null;

  protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente = null;

  protected IAulaPersistente _aulaPersistente = null;
  protected ISalaPersistente _salaPersistente = null;
  protected ITurmaPersistente _turmaPersistente = null;
  protected ITurnoPersistente _turnoPersistente = null;
  protected IFrequentaPersistente _frequentaPersistente = null;
  protected IPersistentEnrolment _inscricaoPersistente = null;  
  protected ITurmaTurnoPersistente _turmaTurnoPersistente = null;
  protected ITurnoAlunoPersistente _turnoAlunoPersistente = null;  
  protected ITurnoAulaPersistente _turnoAulaPersistente = null;

  protected IPersistentStudent persistentStudent = null;

  protected IDepartamentoPersistente departamentoPersistente = null;
  protected IDisciplinaDepartamentoPersistente disciplinaDepartamentoPersistente = null;

  protected IPlanoCurricularCursoPersistente planoCurricularCursoPersistente = null;
  protected IStudentCurricularPlanPersistente studentCurricularPlanPersistente = null; 
  protected IPersistentCountry persistentCountry = null;
  protected IPersistentCurricularCourse persistantCurricularCourse = null;

  protected IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
  protected IPersistentCandidateSituation persistentCandidateSituation = null;
  protected IPersistentExecutionYear persistentExecutionYear = null;
  protected IPersistentExecutionPeriod persistentExecutionPeriod = null;

  private dbaccess _dbAcessPoint = null;
    
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
  		_dbAcessPoint = new dbaccess();
  		_dbAcessPoint.openConnection();
  		_dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
  		_dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		_dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Setup failed: " + ex);
  	}
  	
    ligarSuportePersistente();
  }

  protected void tearDown() {
  	try {
  		_dbAcessPoint.openConnection();
  		_dbAcessPoint.loadDataBase("etc/testBackup.xml");
  		//_dbAcessPoint.loadDataBase("etc/testDataSet.xml");
  		_dbAcessPoint.closeConnection();
  	} catch (Exception ex) {
  		System.out.println("Tear down failed: " +ex);
  	}
  }
            
  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _sitioPersistente = _suportePersistente.getISitioPersistente();
    _seccaoPersistente = _suportePersistente.getISeccaoPersistente();
    _itemPersistente = _suportePersistente.getIItemPersistente();
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();

    _aulaPersistente = _suportePersistente.getIAulaPersistente();
    _salaPersistente = _suportePersistente.getISalaPersistente();
    _turmaPersistente = _suportePersistente.getITurmaPersistente();
    _turnoPersistente = _suportePersistente.getITurnoPersistente();
    
    _frequentaPersistente = _suportePersistente.getIFrequentaPersistente();
    _inscricaoPersistente = _suportePersistente.getIInscricaoPersistente();    
    _turmaTurnoPersistente = _suportePersistente.getITurmaTurnoPersistente();
    _turnoAlunoPersistente = _suportePersistente.getITurnoAlunoPersistente();    
    _turnoAulaPersistente = _suportePersistente.getITurnoAulaPersistente();        

    cursoExecucaoPersistente = _suportePersistente.getICursoExecucaoPersistente();
    cursoPersistente = _suportePersistente.getICursoPersistente();

    _disciplinaExecucaoPersistente = _suportePersistente.getIDisciplinaExecucaoPersistente();
    
    persistentStudent = _suportePersistente.getIPersistentStudent();
    
    departamentoPersistente = _suportePersistente.getIDepartamentoPersistente();
    disciplinaDepartamentoPersistente = _suportePersistente.getIDisciplinaDepartamentoPersistente();
    planoCurricularCursoPersistente = _suportePersistente.getIPlanoCurricularCursoPersistente();
    studentCurricularPlanPersistente = _suportePersistente.getIStudentCurricularPlanPersistente();
    persistentCountry = _suportePersistente.getIPersistentCountry();
    persistantCurricularCourse = _suportePersistente.getIPersistentCurricularCourse();
    persistentMasterDegreeCandidate = _suportePersistente.getIPersistentMasterDegreeCandidate();
    persistentCandidateSituation = _suportePersistente.getIPersistentCandidateSituation();
	persistentExecutionYear = _suportePersistente.getIPersistentExecutionYear();
	persistentExecutionPeriod = _suportePersistente.getIPersistentExecutionPeriod();
 
  }
    
}
