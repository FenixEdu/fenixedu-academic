/*
 * Created on 11/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class QuestionOJBTest extends TestCaseOJB {
	/**
	 * @param testName
	 */
	public QuestionOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(QuestionOJBTest.class);
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

	public void testReadByFileNameAndMetadataId() {
		System.out.println("1-> Test ReadByFileNameAndMetadataId");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(new Integer(1));
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			assertNotNull("there is no metadata with id=1", metadata);
			String xmlFileName = new String("g11.xml");
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question =
				persistentQuestion.readByFileNameAndMetadataId(
					xmlFileName,
					metadata);
			assertNotNull(
				"there is no question for metadata with this xml file name",
				question);
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadExampleQuestionByMetadata() {
		System.out.println("2-> Test ReadExampleQuestionByMetadata");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(new Integer(1));
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			assertNotNull("there is no metadata with id=1", metadata);

			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question =
				persistentQuestion.readExampleQuestionByMetadata(metadata);
			assertNotNull("there is no question for this metadata", question);
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByMetadata() {
		System.out.println("3-> Test ReadByMetadata");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(new Integer(1));
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			assertNotNull("there is no metadata with id=1", metadata);
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			List questions = persistentQuestion.readByMetadata(metadata);
			assertEquals("wrong number of questions", 2, questions.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDelete() {
		System.out.println("4-> Test Delete");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question = new Question(new Integer(3));
			question = (IQuestion) persistentQuestion.readByOId(question, true);
			assertNotNull("there is no question with id=3", question);

			persistentQuestion.delete(question);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(new Integer(1));
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			assertNotNull("there is no metadata with id=1", metadata);
			List questions = persistentQuestion.readByMetadata(metadata);
			assertEquals("wrong number of questions", 1, questions.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
