/*
 * InscricaoOJBTest.java
 * JUnit based test
 *
 * Created on 20 de Outubro de 2002, 17:58
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;

public class EnrolmentOJBTest extends TestCaseOJB {
    public EnrolmentOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(EnrolmentOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
    // FIXME: Not yet used ... 

	public void testVoidToDelete() {
	}
    
    
//  /** Test of readByPlanoCurricularAlunoAndDisciplinaCurricular method, of class ServidorPersistente.OJB.EnrolmentOJB. */
//  public void testreadByPlanoCurricularAlunoAndDisciplinaCurricular() {
//    IEnrolment enrolment = null;
//    // read existing Inscricao
//    
//    ICurricularCourse curricularCourse = null;
//    
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//	
//	assertNotNull(curricularCourse);
//    
//    try {
//      _suportePersistente.iniciarTransaccao();
//            
//      IEnrolment enrolmentCriteria = new Enrolment();
//      
//      ICurricularCourse curricularCourse2 = new CurricularCourse();
//	  curricularCourse2.setCode(curricularCourse.getCode());
//	  curricularCourse2.setName(curricularCourse.getName());      
//
//      enrolmentCriteria.setCurricularCourse(curricularCourse2);
//	  
//	  IStudent student = new Student();
//      student.setNumber(new Integer(45498));
//      student.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
//      
//      IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
//	  studentCurricularPlan.setStudent(student);
//	  studentCurricularPlan.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
//      
//      enrolmentCriteria.setStudentCurricularPlan(studentCurricularPlan);
//      
//      enrolment = (IEnrolment) _inscricaoPersistente.readDomainObjectByCriteria(enrolmentCriteria);
//	  assertNotNull("no enrolment read", enrolment);
//      _suportePersistente.confirmarTransaccao();
//
//	  assertTrue(enrolment.getCurricularCourse().getCode().equals("TFCI"));
//	  assertTrue(enrolment.getCurricularCourse().getName().equals("Trabalho Final de Curso I"));
//	  assertTrue(enrolment.getStudentCurricularPlan().getStudent().getNumber().equals(new Integer(45498)));
//
//	  IEnrolment enrolmentCompareObj = new Enrolment();
//	  enrolmentCompareObj.setCurricularCourse(curricularCourse);
//	  enrolmentCompareObj.setStudentCurricularPlan(enrolment.getStudentCurricularPlan());
//	  
//	  assertEquals("Compare of two equal enrolments failed:", enrolment, enrolmentCompareObj);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
// 	curricularCourse = null;
//
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//        
//    // read unexisting Inscricao
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  IEnrolment enrolmentCriteria = new Enrolment();
//	  CurricularCourse curricularCourse2 = new CurricularCourse();
//	  curricularCourse2.setCode(curricularCourse.getCode());
//	  curricularCourse2.setName(curricularCourse.getName());	  
//	  enrolmentCriteria.setCurricularCourse(curricularCourse2);
//	  IStudent student = new Student();
//	  student.setNumber(new Integer(45498));
//	  student.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
//	  IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
//	  studentCurricularPlan.setStudent(student);
//	  studentCurricularPlan.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
//	  enrolmentCriteria.setStudentCurricularPlan(studentCurricularPlan);
//       
//	  enrolment = (IEnrolment) _inscricaoPersistente.readDomainObjectByCriteria(enrolmentCriteria);
//      _suportePersistente.confirmarTransaccao();
//      assertNull(enrolment);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular 2:fail read unexisting inscricao");
//    }
//  }
//
//  // write new existing inscricao
//  public void testCreateExistingInscricao() {
//    IInscricao inscricao = new Inscricao();
//    try {
//    	_suportePersistente.iniciarTransaccao();
//    	ICurricularCourse dc = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//    	_suportePersistente.confirmarTransaccao();
//
//    	//inscricao.setPlanoCurricularAluno(_planoCurricular1);
//    	inscricao.setDisciplinaCurricular(dc);
//
//      _suportePersistente.iniciarTransaccao();
//      _inscricaoPersistente.lockWrite(inscricao);
//      _suportePersistente.confirmarTransaccao();
//      fail("testCreateExistingInscricao");
//    } catch (ExcepcaoPersistencia ex) {
//      //all is ok
//    }
//  }
//
//  // write new non-existing inscricao
//  public void testCreateNonExistingInscricao() {
//    IInscricao inscricao = new Inscricao();
//    //inscricao.setPlanoCurricularAluno(_planoCurricular1);
//	ICurricularCourse curricularCourse = null;
//
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//	
//    inscricao.setDisciplinaCurricular(curricularCourse);
//    try {
//      _suportePersistente.iniciarTransaccao();
//      _inscricaoPersistente.lockWrite(inscricao);
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testCreateNonExistingInscricao");
//    }
//  }
//
//  /** Test of write method, of class ServidorPersistente.OJB.EnrolmentOJB. */
//  public void testWriteExistingUnchangedObject() {
//    // write inscricao already mapped into memory
//    
//    IInscricao inscricao = null;
//    ICurricularCourse curricularCourse = null;
//    
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      inscricao = _inscricaoPersistente.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//    try {
//      _suportePersistente.iniciarTransaccao();
//      _inscricaoPersistente.lockWrite(inscricao);
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testWriteExistingUnchangedObject");
//    }
//  }
//
//  /** Test of write method, of class ServidorPersistente.OJB.EnrolmentOJB. */
//  public void testWriteExistingChangedObject() {
//    IInscricao inscricao1 = null;
//    IInscricao inscricao2 = null;
//      
//    // write inscricao already mapped into memory
//    try {
//    	_suportePersistente.iniciarTransaccao();
//    	ICurricularCourse cc1 = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//    	ICurricularCourse cc2 = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//    	_suportePersistente.confirmarTransaccao();
//
//        _suportePersistente.iniciarTransaccao();
//        inscricao1 = _inscricaoPersistente.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ cc1);
//        inscricao1.setDisciplinaCurricular(cc2);
//        _suportePersistente.confirmarTransaccao();
// 
//       _suportePersistente.iniciarTransaccao();
//       inscricao2 = _inscricaoPersistente.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ cc2);
//       _suportePersistente.confirmarTransaccao();      
//
//       assertEquals(inscricao2.getDisciplinaCurricular().getCode(), cc2.getCode());
//    } catch (ExcepcaoPersistencia ex) {
//    	fail("testWriteExistingChangedObject");        
//    }
//  }
//
//  /** Test of delete method, of class ServidorPersistente.OJB.EnrolmentOJB. */
//  public void testDeleteInscricao() {
//
//	ICurricularCourse curricularCourse = null;
//
//    try {
//      _suportePersistente.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      _suportePersistente.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//
//    try {
//    	_suportePersistente.iniciarTransaccao();
//    	IInscricao inscricao1 = _inscricaoPersistente.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//    	_suportePersistente.confirmarTransaccao();
//
//      _suportePersistente.iniciarTransaccao();
//      _inscricaoPersistente.delete(inscricao1);
//      _suportePersistente.confirmarTransaccao();
//
//      _suportePersistente.iniciarTransaccao();
//      IInscricao inscricao = _inscricaoPersistente.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//      _suportePersistente.confirmarTransaccao();
//
//      assertEquals(inscricao, null);
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteInscricao");
//    }
//  }
//
//  /** Test of deleteAll method, of class ServidorPersistente.OJB.EnrolmentOJB. */
//  public void testDeleteAll() {
//    try {
//      _suportePersistente.iniciarTransaccao();
//      _inscricaoPersistente.deleteAll();
//      _suportePersistente.confirmarTransaccao();
//
//      _suportePersistente.iniciarTransaccao();
//
//      List result = null;
//      try {
//        Implementation odmg = OJB.getInstance();
//        OQLQuery query = odmg.newOQLQuery();;
//        String oqlQuery = "select inscricao from " + Inscricao.class.getName();
//        query.create(oqlQuery);
//        result = (List) query.execute();
//      } catch (QueryException ex) {
//        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//      }
//      _suportePersistente.confirmarTransaccao();
//      assertNotNull(result);
//      assertTrue(result.isEmpty());
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testDeleteInscricao");
//    }
//
//  }
//    
//    
}
