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

public class DisciplinaExecucaoOJBTest extends TestCaseOJB {
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
  }
    
  protected void tearDown() {
    super.tearDown();
  }

  /** Test of readBySiglaAndAnoLectivAndSiglaLicenciatura method, of class ServidorPersistente.OJB.DisciplinaCurricularDisciplinaExecucaoOJB. */
  public void testReadBySiglaAndAnoLectivoAndSiglaLicenciatura() {
	IDisciplinaExecucao executionCourse = null;
	
	// Read Existing	
	try{
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
		
    } catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}

	assertTrue(executionCourse.getNome().equals("Trabalho Final de Curso I"));
	assertTrue(executionCourse.getSigla().equals("TFCI"));
	assertTrue(executionCourse.getPrograma().equals("programa1"));
	assertTrue(executionCourse.getTheoreticalHours().equals(new Double(0)));
	assertTrue(executionCourse.getPraticalHours().equals(new Double(0)));
	assertTrue(executionCourse.getTheoPratHours().equals(new Double(0)));
	assertTrue(executionCourse.getLabHours().equals(new Double(0)));	
	assertNotNull(executionCourse.getAssociatedCurricularCourses());
	assertTrue(executionCourse.getAssociatedCurricularCourses().size() == 1);
	
	// Read Non Existing
	try{
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEEC");
		assertNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
	
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }



  public void testReadByCurricularYearAndExecutionPeriodAndExecutionDegree() {
	List executionCourseList = null;
	
	try{
		_suportePersistente.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod);
		ICurso degree = cursoPersistente.readBySigla("LEIC");
		assertNotNull(degree);

		IPlanoCurricularCurso degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
		assertNotNull(degreeCurricularPlan);
		ICursoExecucao executionDegree = cursoExecucaoPersistente.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
		assertNotNull(executionDegree);

		_suportePersistente.confirmarTransaccao();

		// Read Existing	
		
		_suportePersistente.iniciarTransaccao();
		executionCourseList = _disciplinaExecucaoPersistente.readByCurricularYearAndExecutionPeriodAndExecutionDegree(new Integer(2), executionPeriod, executionDegree);
		assertEquals(executionCourseList.isEmpty(), false);
		_suportePersistente.confirmarTransaccao();

		// Read Non Existing	
		
		executionCourseList = null;
		_suportePersistente.iniciarTransaccao();
		executionCourseList = _disciplinaExecucaoPersistente.readByCurricularYearAndExecutionPeriodAndExecutionDegree(new Integer(6), executionPeriod, executionDegree);
		assertTrue(executionCourseList.isEmpty());
		_suportePersistente.confirmarTransaccao();

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
		_suportePersistente.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod1 = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod1);
		IExecutionPeriod executionPeriod2 = persistentExecutionPeriod.readByNameAndExecutionYear("3º Semestre", executionYear);		
		assertNotNull(executionPeriod2);

		_suportePersistente.confirmarTransaccao();

		// Read Existing	
		
		_suportePersistente.iniciarTransaccao();
		executionCourseList = _disciplinaExecucaoPersistente.readByExecutionPeriod(executionPeriod1);
		assertEquals(executionCourseList.isEmpty(), false);
		_suportePersistente.confirmarTransaccao();

		// Read Non Existing	
		
		executionCourseList = null;
		_suportePersistente.iniciarTransaccao();
		executionCourseList = _disciplinaExecucaoPersistente.readByExecutionPeriod(executionPeriod2);
		assertTrue(executionCourseList.isEmpty());
		_suportePersistente.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }

  public void testEscreverDisciplinaExecucaoNonExisting () {
	try{
		_suportePersistente.iniciarTransaccao();
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
		assertNotNull(executionYear);
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);		
		assertNotNull(executionPeriod);

		_suportePersistente.confirmarTransaccao();

		IDisciplinaExecucao executionCourseTemp = new DisciplinaExecucao("disc1", "d1", "p1", new Double(0), new Double(0), new Double(0), new Double(0), executionPeriod);
		
		_suportePersistente.iniciarTransaccao();
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
		_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(executionCourseTemp);
		_suportePersistente.confirmarTransaccao();

		// Check Execution Courses again
		
		_suportePersistente.iniciarTransaccao();
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
		_suportePersistente.confirmarTransaccao();

	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }
 
 
  public void testEscreverDisciplinaExecucaoExisting () {
  	// Not tested because of Unique in SQL : UNIQUE KEY U1 (ID_INTERNAL, CODE, KEY_EXECUTION_PERIOD)
  	
  }
  
  
  public void testDeleteExecutionCourse () {
	IDisciplinaExecucao executionCourse = null;

	try{
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
	
		_suportePersistente.iniciarTransaccao();
		List attendsList = _frequentaPersistente.readByExecutionCourse(executionCourse);
		assertEquals(attendsList.isEmpty(), false);
		
		List shiftsList = _turnoPersistente.readByExecutionCourse(executionCourse);
		assertEquals(shiftsList.isEmpty(), false);
		_suportePersistente.confirmarTransaccao();
		
		// Delete Execution Course
		_suportePersistente.iniciarTransaccao();
		_disciplinaExecucaoPersistente.deleteExecutionCourse(executionCourse);
		_suportePersistente.confirmarTransaccao();

		// Checks the consistency
		attendsList = null;
		shiftsList = null;
		
		_suportePersistente.iniciarTransaccao();
		
		attendsList = _frequentaPersistente.readByExecutionCourse(executionCourse);
		assertEquals(attendsList.isEmpty(), true);
		
		shiftsList = _turnoPersistente.readByExecutionCourse(executionCourse);
		assertEquals(shiftsList.isEmpty(), true);
		_suportePersistente.confirmarTransaccao();
	
		executionCourse = null;
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
		
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }


  public void testApagarTodasAsDisciplinasExecucao() {
	IDisciplinaExecucao executionCourse = null;

	try{
		_suportePersistente.iniciarTransaccao();
		executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI","2002/2003","LEIC");
		assertNotNull(executionCourse);
		_suportePersistente.confirmarTransaccao();
	
		_suportePersistente.iniciarTransaccao();
		_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
		_suportePersistente.confirmarTransaccao();
	
		_suportePersistente.iniciarTransaccao();
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
		_suportePersistente.confirmarTransaccao();

			
		
	} catch (ExcepcaoPersistencia ex) {
		fail("    -> Failed Reading Existing");
	}
  }




  
  
}