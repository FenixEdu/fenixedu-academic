/*
 * Created on 11/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class MetadataOJBTest extends TestCaseOJB {

	/**
	 * @param testName
	 */
	public MetadataOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MetadataOJBTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	protected void setUp() {
		super.setUp();

	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadByExecutionCourse() {
		System.out.println("1-> Test ReadByExecutionCourse");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(26));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull(
				"there is no executionCourse with id=26",
				executionCourse);
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			List result =
				persistentMetadata.readByExecutionCourse(executionCourse);
			assertNotNull(
				"there are no available metadatas for this executionCourse",
				result);

			assertEquals(
				"wrong number of available metadatas",
				4,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
