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
//      persistentSupport.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//	
//	assertNotNull(curricularCourse);
//    
//    try {
//      persistentSupport.iniciarTransaccao();
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
//      enrolment = (IEnrolment) persistentEnrollment.readDomainObjectByCriteria(enrolmentCriteria);
//	  assertNotNull("no enrolment read", enrolment);
//      persistentSupport.confirmarTransaccao();
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
//      persistentSupport.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//        
//    // read unexisting Inscricao
//    try {
//      persistentSupport.iniciarTransaccao();
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
//	  enrolment = (IEnrolment) persistentEnrollment.readDomainObjectByCriteria(enrolmentCriteria);
//      persistentSupport.confirmarTransaccao();
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
//    	persistentSupport.iniciarTransaccao();
//    	ICurricularCourse dc = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//    	persistentSupport.confirmarTransaccao();
//
//    	//inscricao.setPlanoCurricularAluno(_planoCurricular1);
//    	inscricao.setDisciplinaCurricular(dc);
//
//      persistentSupport.iniciarTransaccao();
//      persistentEnrollment.lockWrite(inscricao);
//      persistentSupport.confirmarTransaccao();
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
//      persistentSupport.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//	
//    inscricao.setDisciplinaCurricular(curricularCourse);
//    try {
//      persistentSupport.iniciarTransaccao();
//      persistentEnrollment.lockWrite(inscricao);
//      persistentSupport.confirmarTransaccao();
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
//      persistentSupport.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      inscricao = persistentEnrollment.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//    try {
//      persistentSupport.iniciarTransaccao();
//      persistentEnrollment.lockWrite(inscricao);
//      persistentSupport.confirmarTransaccao();
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
//    	persistentSupport.iniciarTransaccao();
//    	ICurricularCourse cc1 = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//    	ICurricularCourse cc2 = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso II", "TFCII");
//    	persistentSupport.confirmarTransaccao();
//
//        persistentSupport.iniciarTransaccao();
//        inscricao1 = persistentEnrollment.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ cc1);
//        inscricao1.setDisciplinaCurricular(cc2);
//        persistentSupport.confirmarTransaccao();
// 
//       persistentSupport.iniciarTransaccao();
//       inscricao2 = persistentEnrollment.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ cc2);
//       persistentSupport.confirmarTransaccao();      
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
//      persistentSupport.iniciarTransaccao();
//	  curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
//      persistentSupport.confirmarTransaccao();
//    } catch (ExcepcaoPersistencia ex) {
//      fail("testReadByPlanoCurricularAlunoAndDisciplinaCurricular:fail read existing inscricao");
//    }
//
//	assertNotNull(curricularCourse);
//
//    try {
//    	persistentSupport.iniciarTransaccao();
//    	IInscricao inscricao1 = persistentEnrollment.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//    	persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      persistentEnrollment.delete(inscricao1);
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
//      IInscricao inscricao = persistentEnrollment.readByPlanoCurricularAlunoAndDisciplinaCurricular(/*_aluno1, */ curricularCourse);
//      persistentSupport.confirmarTransaccao();
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
//      persistentSupport.iniciarTransaccao();
//      persistentEnrollment.deleteAll();
//      persistentSupport.confirmarTransaccao();
//
//      persistentSupport.iniciarTransaccao();
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
//      persistentSupport.confirmarTransaccao();
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
