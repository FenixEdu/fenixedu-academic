/*
 * TurmaOJBTest.java
 * JUnit based test
 *
 * Created on 15 de Outubro de 2002, 8:42
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

import Dominio.Curso;
import Dominio.ICurso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorPersistente.ExcepcaoPersistencia;

public class TurmaOJBTest extends TestCaseOJB {
	public TurmaOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TurmaOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testReadByNome() {
		ITurma turma = null;
		// read existing Turma
		try {
			_suportePersistente.iniciarTransaccao();
			turma = _turmaPersistente.readByNome(_turma1.getNome());
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read existing turma");
		}
		assertEquals("testReadByNome:read existing turma", turma, _turma1);

		// read unexisting Turma
		try {
			_suportePersistente.iniciarTransaccao();
			turma = _turmaPersistente.readByNome("turmaInexistente");
			assertNull(turma);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByNome:fail read unexisting turma");
		}
	}

	// write new existing turma
	public void testCreateExistingTurma() {
		ITurma turma =
			new Turma("turma1", new Integer(1), new Integer(1), curso1);
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(turma);
			_suportePersistente.confirmarTransaccao();
			fail("testCreateExistingTurma");
		} catch (ExcepcaoPersistencia ex) {
			//all is ok
		}
	}

	// write new non-existing turma
	public void testCreateNonExistingTurma() {
		ITurma turma = null;
		try {
			_suportePersistente.iniciarTransaccao();
			ICurso lic = cursoPersistente.readBySigla(curso1.getSigla());
			_suportePersistente.confirmarTransaccao();

			turma = new Turma("turma3", new Integer(1), new Integer(1), lic);

			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(turma);
			_suportePersistente.confirmarTransaccao();
			//      assertTrue(((Item)item).getCodigoInterno() != 0);
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateNonExistingTurma");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testWriteExistingUnchangedObject() {
		// write turma already mapped into memory
		try {
			_suportePersistente.iniciarTransaccao();
			ITurma turma = _turmaPersistente.readByNome(_turma1.getNome());
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(turma);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testWriteExistingUnchangedObject");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testWriteExistingChangedObject() {
		ITurma turma1 = null;
		ITurma turma2 = null;
		// write turma already mapped into memory
		try {
			_suportePersistente.iniciarTransaccao();
			//_turmaPersistente.lockWrite(_turma1);
			turma1 = _turmaPersistente.readByNome(_turma1.getNome());
			turma1.setSemestre(new Integer(2));
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			turma2 = _turmaPersistente.readByNome(turma1.getNome());
			_suportePersistente.confirmarTransaccao();

			assertEquals(turma2.getSemestre(), new Integer(2));
			//assertTrue(turma.getOrdem() == 5);
		} catch (ExcepcaoPersistencia ex) {
			fail("testWriteExistingChangedObject");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.TurmaOJB. */
	public void testDeleteTurma() {
		try {
			_suportePersistente.iniciarTransaccao();
			ITurma turma = _turmaPersistente.readByNome(_turma1.getNome());
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.delete(turma);
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			turma = _turmaPersistente.readByNome(_turma1.getNome());
			_suportePersistente.confirmarTransaccao();

			assertEquals(turma, null);
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurma");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.TurnoOJB. */
	public void testDeleteAll() {
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			List result = null;
			try {
				Implementation odmg = OJB.getInstance();
				OQLQuery query = odmg.newOQLQuery();
				;
				String oqlQuery = "select turma from " + Turma.class.getName();
				query.create(oqlQuery);
				result = (List) query.execute();
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
			_suportePersistente.confirmarTransaccao();
			assertNotNull(result);
			assertTrue(result.isEmpty());
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteTurma");
		}

	}

	public void testReadAll() {
		try {
			List turmas = null;
			/* Testa metodo qdo nao ha turmas na BD */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			turmas = _turmaPersistente.readAll();
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadAll: qdo nao ha turmas na BD",
				turmas.size(),
				0);

			_suportePersistente.iniciarTransaccao();
			ICurso lic1 = cursoPersistente.readBySigla(curso1.getSigla());
			ICurso lic2 = cursoPersistente.readBySigla(curso2.getSigla());
			_suportePersistente.confirmarTransaccao();
			_turma1.setLicenciatura(lic1);
			_turma2.setLicenciatura(lic2);

			/* Testa metodo qdo nao mais do q uma turma na BD */
		_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma2);
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			turmas = _turmaPersistente.readAll();
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadAll_1: qdo ha 2 turmas na BD",
				turmas.size(),
				2);
			assertTrue(
				"testReadAll_2: qdo ha 2 turmas na BD",
				turmas.contains(_turma1));
			assertTrue(
				"testReadAll_3: qdo ha 2 turmas na BD",
				turmas.contains(_turma2));

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadAll");
		}
	}

	public void testreadBySemestreAndAnoCurricularAndSiglaLicenciatura() {
		try {
			List turmas1 = null;
			/* Testa metodo qdo nao ha turmas na BD */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();

				
			_suportePersistente.iniciarTransaccao();
			
			turmas1 =
				_turmaPersistente
					.readBySemestreAndAnoCurricularAndSiglaLicenciatura(
					new Integer(1),
					new Integer(1),
					curso1.getSigla());
			_suportePersistente.confirmarTransaccao();
			
		
			assertEquals(
				"testreadBySemestreAndAnoCurricularAndSiglaLicenciatura: qdo nao ha turmas na BD",
				turmas1.size(),
				0);
		
			_suportePersistente.iniciarTransaccao();
			ICurso cursoTemp1 = cursoPersistente.readBySigla(curso1.getSigla());
			ICurso cursoTemp2 = cursoPersistente.readBySigla(curso2.getSigla());
			_suportePersistente.confirmarTransaccao();
			_turma1.setLicenciatura(cursoTemp1);
			_turma2.setLicenciatura(cursoTemp2);

			/* Testa metodo qdo a turma existe na  BD */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma2);
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			turmas1 =
				_turmaPersistente.readBySemestreAndAnoCurricularAndSiglaLicenciatura(
					_turma1.getSemestre(),
					_turma1.getAnoCurricular(),
					_turma1.getLicenciatura().getSigla());
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testreadBySemestreAndAnoCurricularAndSiglaLicenciatura: qdo a turma existe na BD",
				turmas1.size(),
				1);
			assertTrue(
				"testreadBySemestreAndAnoCurricularAndSiglaLicenciatura: qdo a turma existe na BD",
				turmas1.contains(_turma1));
			
			/* Testa metodo qdo a turma n„o existe na  BD */
			_suportePersistente.iniciarTransaccao();
			turmas1 =
				_turmaPersistente.readBySemestreAndAnoCurricularAndSiglaLicenciatura(
					_turma1.getSemestre(),
					new Integer(1),
			"whateveryouwant");
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testreadBySemestreAndAnoCurricularAndSiglaLicenciatura: qdo a turma n„o existe na BD",
				turmas1.size(),
				0);
			
		} catch (ExcepcaoPersistencia ex) {
			fail("testtestReadBySemestreAndSiglaLicenciatura");
		}
	}

	public void testreadByCriteria() {

		try {
			ITurma queryTurma = new Turma();
			List turmas = null;
			queryTurma.setSemestre(new Integer(1));
			queryTurma.setLicenciatura(new Curso());
			queryTurma.getLicenciatura().setSigla(curso1.getSigla());

			/* Testa metodo qdo nao ha turmas na BD */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
			_suportePersistente.iniciarTransaccao();
			turmas = _turmaPersistente.readByCriteria(queryTurma);
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadByCriteria: qdo nao ha turmas na BD",
				0,
				turmas.size());

			_suportePersistente.iniciarTransaccao();
			ICurso cursoTemp1 = cursoPersistente.readBySigla(curso1.getSigla());
			ICurso cursoTemp2 = cursoPersistente.readBySigla(curso2.getSigla());
			_suportePersistente.confirmarTransaccao();
			_turma1.setLicenciatura(cursoTemp1);
			_turma2.setLicenciatura(cursoTemp2);

			/* Testa metodo qdo ha mais do q uma turma na BD (com a turma certa) */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma2);
			_suportePersistente.confirmarTransaccao();

			queryTurma.setSemestre(_turma1.getSemestre());
			queryTurma.setLicenciatura(new Curso());
			queryTurma.getLicenciatura().setSigla(_turma1.getLicenciatura().getSigla());
			
			_suportePersistente.iniciarTransaccao();
			turmas = _turmaPersistente.readByCriteria(queryTurma);
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadByCriteria: qdo ha  turmas na BD",
				1,
				turmas.size());
			assertTrue(
				"testReadByCriteria: qdo ha turmas na BD turma1",
				turmas.contains(_turma1));
			/* Testa metodo qdo ha mais do q uma turma na BD (com a turma errada) */
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(_turma1);
			_turmaPersistente.lockWrite(_turma2);
			_suportePersistente.confirmarTransaccao();

			queryTurma.setSemestre(_turma1.getSemestre());
			queryTurma.setLicenciatura(new Curso());
			queryTurma.getLicenciatura().setSigla("siglaDelLicenciaturaInexistente");

			_suportePersistente.iniciarTransaccao();
			turmas = _turmaPersistente.readByCriteria(queryTurma);
			_suportePersistente.confirmarTransaccao();
			assertEquals(
				"testReadByCriteria: qdo a turma procurada n√£o existe  BD ",
				turmas.size(),
				0);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Falhou:");
		}

	}

}
