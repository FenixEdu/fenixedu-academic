/*
 * Created on 22/Jul/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 22/Jul/2003
 * fenix-head
 * ServidorPersistente.OJB
 * 
 */
public class SummaryOJBTest extends TestCaseOJB {

	/**
	 * @param testName
	 */
	public SummaryOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SummaryOJBTest.class);
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
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao(new Integer(24));
			executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse,false);
			assertNotNull("there is no executionCourse with id=24",executionCourse);
			List result = persistentSummary.readByExecutionCourse(executionCourse);
			assertNotNull("there are no summaries for this executionCourse",result);
			assertEquals("wrong number of summaries",2,result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
	
	public void testReadByExecutionCourseAndType() {
			System.out.println("2-> Test ReadByExecutionCourseAndType");
			try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentSuport.iniciarTransaccao();
				IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
				IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
				IDisciplinaExecucao executionCourse = new DisciplinaExecucao(new Integer(24));
				executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse,false);
				assertNotNull("there is no executionCourse with id=24",executionCourse);		
				TipoAula tipoAula = new TipoAula(1);
				assertNotNull("there is no class type = 1",tipoAula);
				List result = persistentSummary.readByExecutionCourseAndType(executionCourse, tipoAula);
				assertNotNull("there are no summaries for this executionCourse and type",result);
				assertEquals("wrong number of summaries",2,result.size());
				persistentSuport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("exception: ExcepcaoPersistencia");
			}
		}
		
	public void testDelete() {
				System.out.println("2-> Test Delete");
				try {
					ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
					persistentSuport.iniciarTransaccao();
					IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
					ISummary summary = new Summary(new Integer(261));					
					assertNotNull("there is no summary with id=261",summary);							
					persistentSummary.delete(summary);
					persistentSuport.confirmarTransaccao();
					persistentSuport.iniciarTransaccao();
					IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
					IDisciplinaExecucao executionCourse = new DisciplinaExecucao(new Integer(24));
					executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse,false);
					assertNotNull("there is no executionCourse with id=24",executionCourse);
					List result = persistentSummary.readByExecutionCourse(executionCourse);
					assertNotNull("there are no summaries for this executionCourse",result);
					assertEquals("wrong number of summaries",1,result.size());
					persistentSuport.confirmarTransaccao();

				} catch (ExcepcaoPersistencia e) {
					fail("exception: ExcepcaoPersistencia");
				}
			}
}
