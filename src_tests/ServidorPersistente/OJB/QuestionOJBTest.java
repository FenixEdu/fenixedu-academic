/*
 * Created on 11/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
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
			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(new Integer(1));
			metadata = (IMetadata)persistentMetadata.readByOId(metadata, false);
			assertNotNull("there is no metadata with id=1", metadata);
			String xmlFileName = new String("g11.xml");			
			IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
			IQuestion question = persistentQuestion.readByFileNameAndMetadataId(xmlFileName, metadata);
			assertNotNull("there is no question for metadata with this xml file name", question);
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
