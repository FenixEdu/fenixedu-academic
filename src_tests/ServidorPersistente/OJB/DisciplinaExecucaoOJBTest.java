/*
 * DisciplinaExecucaoOJBTest.java
 * JUnit based test
 *
 * Created on 29 de Novembro de 2002, 18:16
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.DisciplinaExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DisciplinaExecucaoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan = null;
	ICursoPersistente persistentDegree = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IFrequentaPersistente persistentAttend = null;
	ITurnoPersistente persistentShift = null;
	
    public DisciplinaExecucaoOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(DisciplinaExecucaoOJBTest.class);

    return suite;
  }
    
  protected void setUp() {
    super.setUp();
	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
	persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
	persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
	persistentDegreeCurricularPlan = persistentSupport.getIPlanoCurricularCursoPersistente();
	persistentDegree = persistentSupport.getICursoPersistente();
	persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
	persistentAttend = persistentSupport.getIFrequentaPersistente();
	persistentShift = persistentSupport.getITurnoPersistente();
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  /** Test of readBySiglaAndAnoLectivAndSiglaLicenciatura method, of class ServidorPersistente.OJB.DisciplinaCurricularDisciplinaExecucaoOJB. */
  public void testReadBySiglaAndAnoLectivoAndSiglaLicenciatura() {
	IDisciplinaExecucao executionCourse = null;
	
	// Read Existing	
	try{
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		persistentSupport.confirmarTransaccao();
		
    } catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}

	assertTrue(executionCourse.getNome().equals("Trabalho Final de Curso I"));
	assertTrue(executionCourse.getSigla().equals("TFCI"));
	assertTrue(executionCourse.getPrograma().equals("programa1"));
	assertTrue(executionCourse.getTheoreticalHours().equals(new Double(1.5)));
	assertTrue(executionCourse.getPraticalHours().equals(new Double(2)));
	assertTrue(executionCourse.getTheoPratHours().equals(new Double(1.5)));
	assertTrue(executionCourse.getLabHours().equals(new Double(2)));	
	assertNotNull(executionCourse.getAssociatedCurricularCourses());
	assertTrue(executionCourse.getAssociatedCurricularCourses().size() == 1);
	
	// Read Non Existing
	try{
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEEC");
		assertNull(executionCourse);
		persistentSupport.confirmarTransaccao();
	
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }



  public void testReadByCurricularYearAndExecutionPeriodAndExecutionDegree() {
	List executionCourseList = null;
	
	try{
		persistentSupport.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod);
		ICurso degree = persistentDegree.readBySigla("LEIC");
		assertNotNull(degree);

		IPlanoCurricularCurso degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);

		persistentSupport.confirmarTransaccao();

		// Read Existing	
		
		persistentSupport.iniciarTransaccao();
		executionCourseList = persistentExecutionCourse.readByCurricularYearAndExecutionPeriodAndExecutionDegree(new Integer(2), executionPeriod, executionDegree);
		assertEquals(executionCourseList.isEmpty(), false);
		persistentSupport.confirmarTransaccao();

		// Read Non Existing	
		
		executionCourseList = null;
		persistentSupport.iniciarTransaccao();
		executionCourseList = persistentExecutionCourse.readByCurricularYearAndExecutionPeriodAndExecutionDegree(new Integer(6), executionPeriod, executionDegree);
		assertTrue(executionCourseList.isEmpty());
		persistentSupport.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }


  public void testReadByExecutionCourseInitialsAndExecutionPeriod() {
	
	// Not tested ... suposed to disappear
	
  }
  
  public void testReadByExecutionPeriod() {
	List executionCourseList = null;
	
	try{
		persistentSupport.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod1 = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod1);
		IExecutionPeriod executionPeriod2 = persistentExecutionPeriod.readByNameAndExecutionYear("3º Semestre", executionYear);		
		assertNotNull(executionPeriod2);

		persistentSupport.confirmarTransaccao();

		// Read Existing	
		
		persistentSupport.iniciarTransaccao();
		executionCourseList = persistentExecutionCourse.readByExecutionPeriod(executionPeriod1);
		assertEquals(executionCourseList.isEmpty(), false);
		persistentSupport.confirmarTransaccao();

		// Read Non Existing	
		
		executionCourseList = null;
		persistentSupport.iniciarTransaccao();
		executionCourseList = persistentExecutionCourse.readByExecutionPeriod(executionPeriod2);
		assertTrue(executionCourseList.isEmpty());
		persistentSupport.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }

  public void testEscreverDisciplinaExecucaoNonExisting () {
	try{
		persistentSupport.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod);

		persistentSupport.confirmarTransaccao();

		IDisciplinaExecucao executionCourseTemp = new DisciplinaExecucao("disc1", "d1", "p1", new Double(0), new Double(0), new Double(0), new Double(0), executionPeriod);
		
		persistentSupport.iniciarTransaccao();
		// Check existing Execution Courses
		try {
		  Implementation odmg = OJB.getInstance();
		  OQLQuery query = odmg.newOQLQuery();
		  String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
		  query.create(oqlQuery);
		  List result = (List) query.execute();
		  assertEquals(result.size(), 10);
		} catch (QueryException ex) {
		  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		// Write Non Existing	
		persistentExecutionCourse.escreverDisciplinaExecucao(executionCourseTemp);
		persistentSupport.confirmarTransaccao();

		// Check Execution Courses again
		
		persistentSupport.iniciarTransaccao();
		try {
		  Implementation odmg = OJB.getInstance();
		  OQLQuery query = odmg.newOQLQuery();;
		  String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
		  query.create(oqlQuery);
		  List result = (List) query.execute();
		  assertEquals(result.size(), 11);
		} catch (QueryException ex) {
		  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		persistentSupport.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }
 
 
  public void testEscreverDisciplinaExecucaoExisting () {
	try{
		persistentSupport.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod);

		persistentSupport.confirmarTransaccao();

		IDisciplinaExecucao executionCourseTemp = new DisciplinaExecucao("disc1", "d1", "p1", new Double(0), new Double(0), new Double(0), new Double(0), executionPeriod);
		

		// Write Non Existing	
		persistentSupport.iniciarTransaccao();
		persistentExecutionCourse.escreverDisciplinaExecucao(executionCourseTemp);
		persistentSupport.confirmarTransaccao();

		executionCourseTemp = new DisciplinaExecucao("disc1", "d1", "p1", new Double(0), new Double(0), new Double(0), new Double(0), executionPeriod);

		// Write Existing
		try {
			persistentSupport.iniciarTransaccao();
			persistentExecutionCourse.escreverDisciplinaExecucao(executionCourseTemp);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing");
		} catch (ExistingPersistentException ex) {
			assertNotNull("Write Existing: expected exception", ex);
		}

	} catch (ExcepcaoPersistencia ex) {
		fail("Write Existing: unexpected exception");
	}
  }
  
  
  public void testDeleteExecutionCourse () {
	IDisciplinaExecucao executionCourse = null;

	try{
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		persistentSupport.confirmarTransaccao();
	
		persistentSupport.iniciarTransaccao();
		List attendsList = persistentAttend.readByExecutionCourse(executionCourse);
		assertEquals(attendsList.isEmpty(), false);
		
		List shiftsList = persistentShift.readByExecutionCourse(executionCourse);
		assertEquals(shiftsList.isEmpty(), false);
		persistentSupport.confirmarTransaccao();
		
		// Delete Execution Course
		persistentSupport.iniciarTransaccao();
		persistentExecutionCourse.deleteExecutionCourse(executionCourse);
		persistentSupport.confirmarTransaccao();

		// Checks the consistency
		attendsList = null;
		shiftsList = null;
		
		persistentSupport.iniciarTransaccao();
		
		attendsList = persistentAttend.readByExecutionCourse(executionCourse);
		assertEquals(attendsList.isEmpty(), true);
		
		shiftsList = persistentShift.readByExecutionCourse(executionCourse);
		assertEquals(shiftsList.isEmpty(), true);
		persistentSupport.confirmarTransaccao();
	
		executionCourse = null;
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNull(executionCourse);
		persistentSupport.confirmarTransaccao();
		
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }


  public void testApagarTodasAsDisciplinasExecucao() {
	IDisciplinaExecucao executionCourse = null;

	try{
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		persistentSupport.confirmarTransaccao();
	
		persistentSupport.iniciarTransaccao();
		persistentExecutionCourse.apagarTodasAsDisciplinasExecucao();
		persistentSupport.confirmarTransaccao();
	
		persistentSupport.iniciarTransaccao();
		try {
		  Implementation odmg = OJB.getInstance();
		  OQLQuery query = odmg.newOQLQuery();;
		  String oqlQuery = "select all from " + DisciplinaExecucao.class.getName();
		  query.create(oqlQuery);
		  List result = (List) query.execute();
		  assertTrue(result.isEmpty());
		} catch (QueryException ex) {
		  throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		persistentSupport.confirmarTransaccao();

			
		
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }




  
  
}