package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShift;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoAula;
import DataBeans.util.Cloner;

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

		IDisciplinaExecucao executionCourse = null; 
		try {
			_suportePersistente.iniciarTransaccao();
			executionCourse = _disciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura("AC", "2002/2003", "LEIC");
			assertNotNull(executionCourse);
			_suportePersistente.confirmarTransaccao();
		} catch (Exception ex) {
			fail("testReadAll: no Shifts to read");
		}


		InfoShift infoShift = new InfoShift();
		infoShift.setNome("turno1");
		infoShift.setInfoDisciplinaExecucao(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
		
		
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
		} catch (Exception ex) {
			fail("testReadAll: no Shifts to read");
		}
		assertEquals(
			"testReadAll: no Shifts to read aqui",
			0,
			((List) result).size());

		//2 classes in database
		
		ITurno shift1 = new Turno("Turno1", new TipoAula(TipoAula.DUVIDAS), new Integer(20), executionCourse);
		ITurno shift2 = new Turno("Turno2", new TipoAula(TipoAula.RESERVA), new Integer(20), executionCourse);
		
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.lockWrite(shift1);
			_turnoPersistente.lockWrite(shift2);
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


}
