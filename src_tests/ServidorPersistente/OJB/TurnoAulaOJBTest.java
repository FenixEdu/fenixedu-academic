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
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.Sala;
import Dominio.Turno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;

public class TurnoAulaOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null;
	ITurnoAulaPersistente persistentShiftLesson = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	ITurnoPersistente persistentShift = null;
	ISalaPersistente persistentRoom = null;
	IAulaPersistente persistentLesson = null;

	
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
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentShiftLesson = persistentSupport.getITurnoAulaPersistente();
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentShift = persistentSupport.getITurnoPersistente();
		persistentRoom = persistentSupport.getISalaPersistente();
		persistentLesson = persistentSupport.getIAulaPersistente();
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
				persistentSupport.iniciarTransaccao();
				shift =
					persistentSupport
						.getITurnoPersistente()
						.readByNameAndExecutionCourse(
						"turno_apr_teorico1",
						executionCourse);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("Read existing shift : turno_apr_teorico1");
			}

		
			ISala room = new Sala();
			room.setNome("Ga1");
			
			
			try {
				persistentSupport.iniciarTransaccao();
				List lessonRoomList =
					persistentSupport
						.getIAulaPersistente()
						.readByRoomAndExecutionPeriod(
						room,
						executionPeriod);
				lesson = (IAula) lessonRoomList.get(0);
				persistentSupport.confirmarTransaccao();
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
				persistentSupport.iniciarTransaccao();
				shift =
					persistentSupport
						.getITurnoPersistente()
						.readByNameAndExecutionCourse(
						"turno_apr_teorico1",
						executionCourse);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("Read existing shift : turno_apr_teorico1");
			}

		
			ISala room = new Sala();
			room.setNome("Ga1");
			
			
			try {
				persistentSupport.iniciarTransaccao();
				List lessonRoomList =
					persistentSupport
						.getIAulaPersistente()
						.readByRoomAndExecutionPeriod(
						room,
						executionPeriod);
				lesson = (IAula) lessonRoomList.get(0);
				persistentSupport.confirmarTransaccao();
			} catch (Exception e) {
				fail("Reading lesson");
			}

			assertNotNull("Missing shift",shift);
			assertNotNull("Missing lesson", lesson);
			
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.delete(new TurnoAula(shift, lesson));
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			ITurnoAula turnoAula =
				persistentShiftLesson.readByShiftAndLesson(shift, lesson);
			persistentSupport.confirmarTransaccao();
			assertEquals(turnoAula, null);
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurnoAula");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testDeleteAll() {
		try {
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.deleteAll();
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();

				///////////////////////////////////////////////////////////////////
				// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
				///////////////////////////////////////////////////////////////////
				Database db = odmg.newDatabase();

				try {
					db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
				} catch (ODMGException e) {
					e.printStackTrace();
				}
				///////////////////////////////////////////////////////////////////
				// End of Added Code
				///////////////////////////////////////////////////////////////////

				OQLQuery query = odmg.newOQLQuery();

				String oqlQuery =
					"select turnoAula from " + TurnoAula.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			persistentSupport.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurnoAula");
		}

	}

	public void testReadAulasDeTurno() {
		try {
			List aulas = null;
      
			persistentSupport.iniciarTransaccao();
			IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
			assertNotNull(executionCourse);

			/* Testa metodo qdo ha mais do q uma aula de um turno na BD */
			aulas =
				persistentShiftLesson.readByShift(
					new Turno("turno4", null, null, executionCourse));
			persistentSupport.confirmarTransaccao();
			
			assertEquals(
				"testReadAulasDeTurno: qdo ha 2 aulas da turma na BD",
				aulas.size(),
				1);

			/* Testa metodo qdo nao ha nenhuma aula do turno */
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.deleteAll();
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			aulas =
				persistentShiftLesson.readByShift(
					new Turno("turno1", null, null, executionCourse));
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadAulasDeTurno: qdo nao nenhuma aula do turno na BD",
				aulas.size(),
				0);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAulasDeTurno");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testDeleteTurnoAulaByKeys() {
		
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 8);
		startTime.set(Calendar.MINUTE, 00);
		startTime.set(Calendar.SECOND, 00);
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 9);
		endTime.set(Calendar.MINUTE, 30);
		endTime.set(Calendar.SECOND, 00);

	    try {
	      persistentSupport.iniciarTransaccao();
		  IDisciplinaExecucao executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCII", "2002/2003", "LEEC");
		  assertNotNull(executionCourse);
  
		  ITurno shift = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse);
		  assertNotNull(shift);

		  ISala room = persistentRoom.readByName("Ga1");
		  assertNotNull(room);
		  DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
		  	      
	      persistentShiftLesson.delete(shift, weekDay, startTime, endTime, room);
	      persistentSupport.confirmarTransaccao();
	
	
	      persistentSupport.iniciarTransaccao();
	      IAula lesson = persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(weekDay, startTime, endTime, room);
	      ITurnoAula turnoAula = persistentShiftLesson.readByShiftAndLesson(shift, lesson);
		  assertNull(turnoAula);
	
	      persistentSupport.confirmarTransaccao();
	
	    } catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurnoAula");
	    }
	}

	/** Test of ReadLessonsByStudent method, of class ServidorPersistente.OJB.TurnoAulaOJB. */
	public void testReadLessonsByStudent() {
		try {
			List lessons = null;

			persistentSupport.iniciarTransaccao();
			lessons = persistentShiftLesson.readLessonsByStudent("45498");
			persistentSupport.confirmarTransaccao();
			assertNotNull("testReadLessonsByStudent", lessons);
			assertEquals("testReadLessonsByStudent", lessons.size(), 1);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadLessonsByStudent");
		}
	}

	/**
	 * Method updateWithModifications.
	 * @param turnoAula
	 */
	private void updateWithModifications(ITurnoAula shiftLesson) {
		try {
			/** get with keys */
			persistentSupport.iniciarTransaccao();
			 shiftLesson = persistentShiftLesson.readByShiftAndLesson(shiftLesson.getTurno(), shiftLesson.getAula());
			persistentSupport.confirmarTransaccao();
				
			persistentSupport.iniciarTransaccao();
			List lessonList = (List) persistentLesson.readByExecutionCourse(shiftLesson.getTurno().getDisciplinaExecucao());
			
			IAula lesson = (IAula) lessonList.get(1);
			persistentSupport.confirmarTransaccao();		

			shiftLesson.setAula(lesson);
			
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.lockWrite(shiftLesson);
			persistentSupport.confirmarTransaccao();
						
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
			persistentSupport.iniciarTransaccao();
			shiftLesson = persistentShiftLesson.readByShiftAndLesson(shiftLesson.getTurno(), shiftLesson.getAula());
			persistentSupport.confirmarTransaccao();
				
		
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.lockWrite(shiftLesson);
			persistentSupport.confirmarTransaccao();
						
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
				persistentSupport.iniciarTransaccao();
				persistentShiftLesson.lockWrite(createdShiftLesson);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Creating new shiftLesson");
			}

			ITurnoAula shiftLesson = null;
			try {
				persistentSupport.iniciarTransaccao();
				shiftLesson =
					persistentShiftLesson.readByShiftAndLesson(shift, lesson);
				persistentSupport.confirmarTransaccao();
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
			persistentSupport.iniciarTransaccao();
			persistentShiftLesson.lockWrite(turnoAula);
			persistentSupport.confirmarTransaccao();
			fail("testCreateExistingTurnoAula: Expected an Exception");
		} catch (ExistingPersistentException ex) {
			assertNotNull("testCreateExistingTurnoAula");
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateExistingTurnoAula: Unexpected Excpetion");
		}
	}


}
