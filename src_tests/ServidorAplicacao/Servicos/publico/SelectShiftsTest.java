package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author João Mota
 *
 */
public class SelectShiftsTest extends TestCaseServicos {

	/**
	 * Constructor for SelectShiftsTest.
	 * @param testName
	 */
	public SelectShiftsTest(String testName) {
		super(testName);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectShiftsTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {
		Object argsSelectShifts[] = new Object[1];
		argsSelectShifts[0] = new InfoShift();
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadAll: no Shifts to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: no Shifts to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.lockWrite(_turno1);
			_turnoPersistente.lockWrite(_turno3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadAll: 2 Shifts in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: 2 Shifts in db");
		}
	}

	public void testReadByName() {
		Object argsSelectShifts[] = new Object[1];
		InfoShift infoShift = new InfoShift();
		infoShift.setNome(_turno1.getNome());
		argsSelectShifts[0] = infoShift;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadByName: no Shifts to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: no Shifts to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.lockWrite(_turno1);
			_turnoPersistente.lockWrite(_turno3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadByName: 2 Shifts in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: 2 Shifts in db");
		}
	}

	public void testReadByExecutionCourse() {
		Object argsSelectShifts[] = new Object[1];
		InfoShift infoShift = new InfoShift();
		InfoExecutionCourse infoExeCourse = new InfoExecutionCourse();
		infoExeCourse.setNome(_disciplinaExecucao1.getNome());
		infoExeCourse.setSigla(_disciplinaExecucao1.getSigla());
		infoExeCourse.setPrograma(_disciplinaExecucao1.getPrograma());
		infoShift.setInfoDisciplinaExecucao(infoExeCourse);
		argsSelectShifts[0] = infoShift;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadByExecutionCourse: no Shifts to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByExecutionCourse: no Shifts to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.lockWrite(_turno1);
			_turnoPersistente.lockWrite(_turno3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectShifts", argsSelectShifts);
			assertEquals(
				"testReadByExecutionCourse: 2 Shifts in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByExecutionCourse: 2 Shifts in db");
		}

	}

	

}
