package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jmota
 *
 */
public class SelectClassesTest extends TestCaseServicos {

	
	private InfoDegree infoDegree = null;
	/**
	 * Constructor for SelectClassesTest.
	 */
	public SelectClassesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectClassesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	public void testReadAll() {
		Object argsSelectClasses[] = new Object[1];
		argsSelectClasses[0] = new InfoClass();
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadAll: no classes to read aqui",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.lockWrite(_curso1);
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadAll: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: 2 classes in db");
		}
	}

	public void testReadByName() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();
		infoClass.setNome("turma1");
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByName: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.lockWrite(_curso1);
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByName: 2 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByName: 2 classes in db");
		}
	}

	public void testReadBySemester() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();
		infoClass.setSemestre(_turma1.getSemestre());
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadBySemester: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadBySemester: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.lockWrite(_curso1);
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadBySemester: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadBySemester: 2 classes in db");
		}
	}

	public void testReadByCurricularYear() {
		Object argsSelectClasses[] = new Object[1];
		InfoClass infoClass = new InfoClass();
		infoClass.setAnoCurricular(_turma1.getAnoCurricular());
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByCurricularYear: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByCurricularYear: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.lockWrite(_curso1);
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByCurricularYear: 2 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByCurricularYear: 2 classes in db");
		}
	}

	public void testReadByDegree() {
		Object argsSelectClasses[] = new Object[1];
		InfoDegree infoDegree = new InfoDegree();
		infoDegree.setNome(_curso1.getNome());
		infoDegree.setSigla(_curso1.getSigla());
		InfoClass infoClass = new InfoClass();
		infoClass.setInfoLicenciatura(infoDegree);
		argsSelectClasses[0] = infoClass;
		Object result = null;

		//Empty database	
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegree: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegree: no classes to read");
		}

		//2 classes in database
		try {
			_suportePersistente.iniciarTransaccao();
			_cursoPersistente.lockWrite(_curso1);
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma3);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegree: 2 classes in db",
				2,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegree: 2 classes in db");
		}
	}

}
