package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
		try {
			_gestor=GestorServicos.manager();
			_suportePersistente= SuportePersistenteOJB.getInstance();
			_turmaPersistente=_suportePersistente.getITurmaPersistente();
			persistentExecutionYear=_suportePersistente.getIPersistentExecutionYear();
			persistentExecutionPeriod=_suportePersistente.getIPersistentExecutionPeriod();
			_cursoPersistente=_suportePersistente.getICursoPersistente();
			_persistentDegreeCurricularPlan=_suportePersistente.getIPlanoCurricularCursoPersistente();
			_cursoExecucaoPersistente=_suportePersistente.getICursoExecucaoPersistente();
		} catch (ExcepcaoPersistencia e) {
			fail("setup failed");
			e.printStackTrace();
		}
	}

	protected void tearDown() {
		super.tearDown();
	}

	



	public void testReadByDegreeAndOtherStuff() {
		Object argsSelectClasses[] = new Object[1];
		InfoDegree infoDegree = new InfoDegree();

		ITurma class1 = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(executionPeriod);

			degree = _cursoPersistente.readBySigla("LEIC");
			assertNotNull(degree);
			degreeCurricularPlan =
				_persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					degree);
			assertNotNull(degreeCurricularPlan);
			executionDegree =
				_cursoExecucaoPersistente
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);
			class1 =
				_turmaPersistente
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					"10501",
					executionDegree,
					executionPeriod);
			assertNotNull(class1);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		infoDegree.setNome(degree.getNome());
		infoDegree.setSigla(degree.getSigla());

		InfoClass infoClass = Cloner.copyClass2InfoClass(class1);
		
		
		
		argsSelectClasses[0] = infoClass;
		Object result = null;
System.out.println("anocurricular"+infoClass.getAnoCurricular());
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
				_gestor.executar(null, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegreeAndOtherStuff: no classes to read",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegreeAndOtherStuff: no classes to read");
		}

		//2 classes in database
		ITurma classTemp1 = null;
		ITurma classTemp2 = null;

		try {
			_suportePersistente.iniciarTransaccao();
			classTemp1 =
				new Turma(
					"10501",
					new Integer(1),
					executionDegree,
					executionPeriod);
			
			classTemp2 =
				new Turma(
					"10502",
					new Integer(3),
					executionDegree,
					executionPeriod);
			
			_turmaPersistente.lockWrite(classTemp1);
			_turmaPersistente.lockWrite(classTemp2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "SelectClasses", argsSelectClasses);
			assertEquals(
				"testReadByDegreeAndOtherStuff: 2 classes in db",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadByDegreeAndOtherStuff: 2 classes in db");
		}
	}

}
