/*
 * TurnoAulaOJBTest.java
 * JUnit based test
 *
 * Created on 22 de Outubro de 2002, 9:23
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;

public class TurnoAulaOJBTest extends TestCaseOJB {

	
	public TurnoAulaOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TurnoAulaOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();

	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testWriteShiftLesson() {
		try {

			ITurnoAula createdShiftLesson = null;
			ITurno shift = null;
			IAula lesson = null;

			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			IExecutionYear executionYear = new ExecutionYear();
			executionYear.setYear("2002/2003");
			executionPeriod.setName("2º Semestre");
			executionPeriod.setExecutionYear(executionYear);
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setSigla("APR");
			executionCourse.setExecutionPeriod(executionPeriod);

		
			try {
				_suportePersistente.iniciarTransaccao();
				shift =
					_suportePersistente
						.getITurnoPersistente()
						.readByNameAndExecutionCourse(
						"turno_apr_teorico1",
						executionCourse);
				_suportePersistente.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("Read existing shift : turno_apr_teorico1");
			}

		
			ISala room = new Sala();
			room.setNome("Ga1");
			
			
			try {
				_suportePersistente.iniciarTransaccao();
				List lessonRoomList =
					_suportePersistente
						.getIAulaPersistente()
						.readByRoomAndExecutionPeriod(
						room,
						executionPeriod);
				lesson = (IAula) lessonRoomList.get(0);
				_suportePersistente.confirmarTransaccao();
			} catch (Exception e) {
				fail("Reading lesson");
			}
		

			write(shift, lesson);
			
			writeDuplicateShiftLesson(shift, lesson);
						
			update(new TurnoAula(shift, lesson));
			
			updateWithModifications(new TurnoAula(shift, lesson));

		} catch (Exception e) {
			e.printStackTrace(System.out);
			fail("testCreateNonExistingShiftLesson");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testDeleteTurnoAula() {
		try {
			ITurnoAula createdShiftLesson = null;
			ITurno shift = null;
			IAula lesson = null;

			IExecutionPeriod executionPeriod = new ExecutionPeriod();
			IExecutionYear executionYear = new ExecutionYear();
			executionYear.setYear("2002/2003");
			executionPeriod.setName("2º Semestre");
			executionPeriod.setExecutionYear(executionYear);
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setSigla("APR");
			executionCourse.setExecutionPeriod(executionPeriod);

		
			try {
				_suportePersistente.iniciarTransaccao();
				shift =
					_suportePersistente
						.getITurnoPersistente()
						.readByNameAndExecutionCourse(
						"turno_apr_teorico1",
						executionCourse);
				_suportePersistente.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("Read existing shift : turno_apr_teorico1");
			}

		
			ISala room = new Sala();
			room.setNome("Ga1");
			
			
			try {
				_suportePersistente.iniciarTransaccao();
				List lessonRoomList =
					_suportePersistente
						.getIAulaPersistente()
						.readByRoomAndExecutionPeriod(
						room,
						executionPeriod);
				lesson = (IAula) lessonRoomList.get(0);
				_suportePersistente.confirmarTransaccao();
			} catch (Exception e) {
				fail("Reading lesson");
			}

			assertNotNull("Missing shift",shift);
			assertNotNull("Missing lesson", lesson);
			
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.delete(new TurnoAula(shift, lesson));
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			ITurnoAula turnoAula =
				_turnoAulaPersistente.readByShiftAndLesson(shift, lesson);
			_suportePersistente.confirmarTransaccao();
			assertEquals(turnoAula, null);
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurnoAula");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testDeleteAll() {
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();
				OQLQuery query = odmg.newOQLQuery();

				String oqlQuery =
					"select turnoAula from " + TurnoAula.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			_suportePersistente.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurnoAula");
		}

	}

	public void testReadAulasDeTurno() {
		try {
			List aulas = null;

			/* Testa metodo qdo ha mais do q uma aula de um turno na BD */
			_suportePersistente.iniciarTransaccao();
			aulas =
				_turnoAulaPersistente.readByShift(
					new Turno("455", null, null, null));
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadAulasDeTurno: qdo ha 2 aulas da turma na BD",
				aulas.size(),
				2);

			/* Testa metodo qdo nao ha nenhuma aula do turno */
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			aulas =
				_turnoAulaPersistente.readByShift(
					new Turno("turno1", null, null, null));
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadAulasDeTurno: qdo nao nenhuma aula do turno na BD",
				aulas.size(),
				0);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAulasDeTurno");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
//	public void testDeleteTurnoAulaByKeys() {
//		//    try {
//		//      _suportePersistente.iniciarTransaccao();
//		//      _turnoAulaPersistente.delete(_turno3.getNome(), _aula1.getDiaSemana(), _aula1.getInicio(),
//		//                                   _aula1.getFim(), _aula1.getSala());
//		//      _suportePersistente.confirmarTransaccao();
//		//
//		//      _suportePersistente.iniciarTransaccao();
//		//      IAula aula1 = _aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(_diaSemana1, _inicio, _fim, _sala1);
//		//      ITurno turno3 = _turnoPersistente.readByNome(_turno3.getNome());
//		//      _suportePersistente.confirmarTransaccao();
//		// 
//		//      _suportePersistente.iniciarTransaccao();
//		//      ITurnoAula turnoAula = _turnoAulaPersistente.readByTurnoAndAula(turno3, aula1);
//		//      _suportePersistente.confirmarTransaccao();
//		//
//		//      assertEquals(turnoAula, null);
//		//    } catch (ExcepcaoPersistencia ex) {
//		fail("testDeleteTurnoAula");
//		//    }
//	}

	/** Test of ReadLessonsByStudent method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testReadLessonsByStudent() {
		try {
			List lessons = null;

			_suportePersistente.iniciarTransaccao();
			lessons = _turnoAulaPersistente.readLessonsByStudent("45498");
			_suportePersistente.confirmarTransaccao();
			assertNotNull("testReadLessonsByStudent", lessons);
			assertEquals("testReadLessonsByStudent", lessons.size(), 2);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadLessonsByStudent");
		}
	}

	/** Test of ReadLessonsByShift method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testReadLessonsByShift() {
		//		  try {
		//			  List lessons = null;
		//
		//			  _suportePersistente.iniciarTransaccao();
		//			  lessons = _turnoAulaPersistente.readLessonsByShift("turno_apr_teorico1");
		//			  _suportePersistente.confirmarTransaccao();
		//			  assertNotNull("testReadLessonsByStudent", lessons);
		//			  assertEquals("testReadLessonsByStudent", lessons.size(), 1);
		//
		//		  } catch (ExcepcaoPersistencia ex) {
		fail("testReadLessonsByStudent");
		//		  }
	}
	/**
	 * Method updateWithModifications.
	 * @param turnoAula
	 */
	private void updateWithModifications(ITurnoAula shiftLesson) {
		try {
			/** get with keys */
			_suportePersistente.iniciarTransaccao();
			 shiftLesson = _turnoAulaPersistente.readByShiftAndLesson(shiftLesson.getTurno(), shiftLesson.getAula());
			_suportePersistente.confirmarTransaccao();
				
			_suportePersistente.iniciarTransaccao();
			List lessonList = (List) _aulaPersistente.readByExecutionCourse(shiftLesson.getTurno().getDisciplinaExecucao());
			
			IAula lesson = (IAula) lessonList.get(1);
			_suportePersistente.confirmarTransaccao();		

			shiftLesson.setAula(lesson);
			
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.lockWrite(shiftLesson);
			_suportePersistente.confirmarTransaccao();
						
		} catch (Exception e) {
			/* to lockWrite shift and lesson must be mapped */
			fail("Update (with modification) shift lesson");
		}
		
	}

	/**
	 * Method update.
	 * @param shift 
	 * @param lesson
	 */
	private void update(ITurnoAula shiftLesson) {
		try {
			/** get with keys */
			_suportePersistente.iniciarTransaccao();
			shiftLesson = _turnoAulaPersistente.readByShiftAndLesson(shiftLesson.getTurno(), shiftLesson.getAula());
			_suportePersistente.confirmarTransaccao();
				
		
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.lockWrite(shiftLesson);
			_suportePersistente.confirmarTransaccao();
						
		} catch (Exception e) {
			/* to lockWrite shift and lesson must be mapped */
			fail("Update (no modification) shift lesson");
		}

	}

	private void write(ITurno shift, IAula lesson) {
		try {
			ITurnoAula createdShiftLesson = null;
			try {
				createdShiftLesson = new TurnoAula(shift, lesson);
				_suportePersistente.iniciarTransaccao();
				_turnoAulaPersistente.lockWrite(createdShiftLesson);
				_suportePersistente.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Creating new shiftLesson");
			}

			ITurnoAula shiftLesson = null;
			try {
				_suportePersistente.iniciarTransaccao();
				shiftLesson =
					_turnoAulaPersistente.readByShiftAndLesson(shift, lesson);
				_suportePersistente.confirmarTransaccao();
				assertNotNull(
					"ShiftLesson readed after creation must be not null",
					shiftLesson);
				assertEquals(shiftLesson, createdShiftLesson);
			} catch (ExcepcaoPersistencia e) {
				fail("Reading already created shiftLesson");
			}
		} catch (Exception e) {
			fail("writing new shiftLesson");
		}
	}

	private void writeDuplicateShiftLesson(ITurno shift, IAula lesson) {
		ITurnoAula turnoAula = new TurnoAula(shift, lesson);
		
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoAulaPersistente.lockWrite(turnoAula);
			_suportePersistente.confirmarTransaccao();
			fail("testCreateExistingTurnoAula");
		} catch (ExcepcaoPersistencia ex) {
			//all is ok
		}
	}


}
