/*
 * Created on 11/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;

public class BibliographicReferenceOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	/**
	 * @param testName
	 */
	public BibliographicReferenceOJBTest(String testName) {
		super(testName);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(BibliographicReferenceOJBTest.class);
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

		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();

	}

	public void testReadExistingBibliographicReference() {
		IDisciplinaExecucao executionCourse = null;
		IBibliographicReference reference = null;

		// reads an execution course to serve as input to the next invoked method
		try {
			persistentSupport.iniciarTransaccao();
			//			executionCourse =
			//				persistentExecutionCourse
			//					.readByExecutionCourseInitialsAndExecutionPeriod(
			//					"TFCI",
			//					new ExecutionPeriod(
			//						"2º Semestre",
			//						new ExecutionYear("2002/2003")));
			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Execution Course");
		}

		// reads a bibliographic reference which exists in the database
		try {
			persistentSupport.iniciarTransaccao();
			IPersistentBibliographicReference ref =
				persistentSupport.getIPersistentBibliographicReference();
			reference =
				ref.readBibliographicReference(
					executionCourse,
					"xpto",
					"pedro",
					"ref",
					"2002");
			assertNotNull(reference);
			assertTrue(reference.getTitle().equals("xpto"));
			assertTrue(reference.getAuthors().equals("pedro"));
			assertTrue(reference.getReference().equals("ref"));
			assertTrue(reference.getYear().equals("2002"));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Bibliographic Reference");
		}
	}

	public void testReadNonExistingBibliographicReference() {
		IDisciplinaExecucao executionCourse = null;
		IBibliographicReference reference = null;

		// reads an execution course to serve as input to the next invoked method
		try {
			persistentSupport.iniciarTransaccao();
			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Execution Course");
		}

		// reads a bibliographic reference which exists in the database		
		try {
			persistentSupport.iniciarTransaccao();
			System.out.println("executionCourse : " + executionCourse);
			IPersistentBibliographicReference ref =
				persistentSupport.getIPersistentBibliographicReference();
			reference =
				ref.readBibliographicReference(
					executionCourse,
					"xptodefrfre",
					"pedro",
					"ref",
					"2002");
			assertNull(reference);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Bibliographic Reference");
		}
	}

		public void testReadBibliographicReferencesOfExecutionCourse() {
		IDisciplinaExecucao executionCourse = null;
		List references = null;

		try {
			persistentSupport.iniciarTransaccao();

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Execution Course");
		}
		
		try {
			persistentSupport.iniciarTransaccao();
			IPersistentBibliographicReference ref =
				persistentSupport.getIPersistentBibliographicReference();
			references = ref.readBibliographicReference(executionCourse);								
			assertNotNull(references);
			assertEquals(references.size(),3);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Bibliographic Reference");
		}
	}
	
	public void testDeleteExistingBibliographicReference() {
			IDisciplinaExecucao executionCourse = null;
			IBibliographicReference reference = null;

			// reads an execution course to serve as input to the next invoked method
			try {
				persistentSupport.iniciarTransaccao();
				executionCourse =
					persistentExecutionCourse
						.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
						"TFCI",
						"2002/2003",
						"LEIC");
				assertNotNull(executionCourse);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("    -> Failed Reading Existing Execution Course");
			}

			// reads a bibliographic reference which exists in the database
			try {
				persistentSupport.iniciarTransaccao();
				IPersistentBibliographicReference ref =
					persistentSupport.getIPersistentBibliographicReference();
				reference =
					ref.readBibliographicReference(
						executionCourse,
						"xpto",
						"pedro",
						"ref",
						"2002");
				ref.delete(reference);
				persistentSupport.confirmarTransaccao();
				persistentSupport.iniciarTransaccao();
				IBibliographicReference newBibRef = null;
				newBibRef =
					ref.readBibliographicReference(
						executionCourse,
						"xpto",
						"pedro",
						"ref",
						"2002");
				assertNull(newBibRef);
				persistentSupport.confirmarTransaccao();

			} catch (ExcepcaoPersistencia ex) {
				fail("    -> Failed Reading Existing Bibliographic Reference");
			}
		}

		public void testDeleteNonExistingBibliographicReference() {
			IDisciplinaExecucao executionCourse = null;
			IBibliographicReference reference = null;

			// reads an execution course to serve as input to the next invoked method
			try {
				persistentSupport.iniciarTransaccao();
				executionCourse =
					persistentExecutionCourse
						.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
						"TFCI",
						"2002/2003",
						"LEIC");
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("    -> Failed Reading Existing Execution Course");
			}

			// reads a bibliographic reference which exists in the database
			try {
				persistentSupport.iniciarTransaccao();
				IPersistentBibliographicReference ref =
					persistentSupport.getIPersistentBibliographicReference();
				reference = new BibliographicReference();
				reference.setExecutionCourse(executionCourse);
				reference.setTitle("fvrtgtr");
				reference.setAuthors("cref");
				reference.setReference("ferj");
				reference.setYear("ehri");
				ref.delete(reference);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("    -> Failed Reading Existing Bibliographic Reference");
			}
		}


		
	protected void tearDown() {
		//super.tearDown();
	}
}
