package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 */
public class SelectExecutionCourseTest extends TestCaseServicos {

	private InfoDegree infoDegree = null;
	/**
	 * Constructor for SelectClassesTest.
	 */
	public SelectExecutionCourseTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectExecutionCourseTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			_gestor = GestorServicos.manager();
			_suportePersistente = SuportePersistenteOJB.getInstance();
			_turmaPersistente = _suportePersistente.getITurmaPersistente();
			persistentExecutionYear =
				_suportePersistente.getIPersistentExecutionYear();
			persistentExecutionPeriod =
				_suportePersistente.getIPersistentExecutionPeriod();
			_cursoPersistente = _suportePersistente.getICursoPersistente();
			_persistentDegreeCurricularPlan =
				_suportePersistente.getIPlanoCurricularCursoPersistente();
			_cursoExecucaoPersistente =
				_suportePersistente.getICursoExecucaoPersistente();
			_disciplinaExecucaoPersistente =
				_suportePersistente.getIDisciplinaExecucaoPersistente();
			_disciplinaCurricularPersistente=_suportePersistente.getIPersistentCurricularCourse();
		} catch (ExcepcaoPersistencia e) {
			fail("setup failed");
			e.printStackTrace();
		}
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {

		
		List result = null;

		InfoExecutionYear infoExecutionYear= new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod= new InfoExecutionPeriod("2º Semestre",infoExecutionYear);
		Integer curricularYear=new Integer(2);
		InfoDegree infoDegree= new InfoDegree("LEIC","Licenciatura de Engenharia Informatica e de Computadores");
		InfoDegreeCurricularPlan plan = new InfoDegreeCurricularPlan("plano1",infoDegree);
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(plan,infoExecutionYear);
		
		Object argsSelectExecutionCourses[] = {infoExecutionDegree,infoExecutionPeriod,curricularYear};
		GestorServicos manager = GestorServicos.manager();
		try {
			result=(List) manager.executar(null,"SelectExecutionCourse",argsSelectExecutionCourses);		
		} catch (Exception e) {
			fail("test reading execution courses");
			e.printStackTrace();
		}
		assertNotNull("test reading executionCourses",result);
		assertTrue("test reading blalbla",result.size()>0 );
}}