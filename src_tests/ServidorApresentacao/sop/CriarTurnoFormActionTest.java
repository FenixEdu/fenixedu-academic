package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.Departamento;
import Dominio.DisciplinaDepartamento;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.ITurno;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Privilegio;
import Dominio.Turno;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;

import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author tfc130
 */
public class CriarTurnoFormActionTest extends MockStrutsTestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected ITurnoPersistente _turnoPersistente = null;
	protected IPersistentCurricularCourse _disciplinaCurricularPersistente =
		null;
	protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente =
		null;
	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected IDepartamentoPersistente _departamentoPersistente = null;
	protected IPessoaPersistente _pessoaPersistente = null;
	protected IPlanoCurricularCursoPersistente _persistentDegreeCurricularPlan =
		null;
	protected IDisciplinaDepartamentoPersistente _persistentDepartmentCourse =
		null;
	protected IDisciplinaDepartamentoPersistente _disciplinaDepartamentoPersistente = null;

	protected ITurno _turno1 = null;
	protected ICurricularCourse _disciplinaCurricular1 = null;
	protected ICurricularCourse _disciplinaCurricular2 = null;
	protected IDisciplinaExecucao _disciplinaExecucao1 = null;
	protected IDisciplinaExecucao _disciplinaExecucao2 = null;
	protected ICurso _curso1 = null;
	protected ICurso _curso2 = null;
	protected ICursoExecucao _cursoExecucao1 = null;
	protected ICursoExecucao _cursoExecucao2 = null;
	protected IPessoa _pessoa1 = null;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarTurnoFormActionTest.class);

		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

		ligarSuportePersistente();
		cleanData();
		_suportePersistente.iniciarTransaccao();
		_curso1 =
			new Curso(
				"LEIC",
				"Informatica",
				new TipoCurso(TipoCurso.LICENCIATURA));
		_cursoPersistente.lockWrite(_curso1);
		_curso2 =
			new Curso("LEGI", "Gestao", new TipoCurso(TipoCurso.LICENCIATURA));

		_cursoExecucao1 = new CursoExecucao("2002/03", _curso1);
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao1);
		_cursoExecucao2 = new CursoExecucao("2003/04", _curso2);
		_cursoExecucaoPersistente.lockWrite(_cursoExecucao2);

		IDepartamento departamento =
			new Departamento("Departamento de Engenharia Informatica", "DEI");
		IDisciplinaDepartamento departmentCourse =
			new DisciplinaDepartamento(
				"Engenharia da Programacao",
				"ep",
				departamento);
		IPlanoCurricularCurso degreeCurricularPlan =
			new PlanoCurricularCurso("plano1", "pc1", _curso1);

		_disciplinaCurricular1 =
			new CurricularCourse(
		new Double(4.0),
new Double(10.0),
new Double(5.0),
new Double(3.0),
new Double(2.0),
new Integer(5),
new Integer(1),
		"Trabalho Final Curso",
		"TFC",
		departmentCourse,
		degreeCurricularPlan);
_disciplinaCurricular2 =
	new CurricularCourse(
new Double(5.0),
new Double(11.0),
new Double(6.0),
new Double(4.0),
new Double(3.0),
		new Integer(5),
		new Integer(2),
				"Trabalho Final Curso2",
				"TFC2",
				departmentCourse,
				degreeCurricularPlan);
		_disciplinaCurricularPersistente.writeCurricularCourse(
			_disciplinaCurricular1);
		_disciplinaCurricularPersistente.writeCurricularCourse(
			_disciplinaCurricular2);
		_disciplinaExecucao1 =
		_disciplinaExecucao1 =
			new DisciplinaExecucao(
				"Trabalho Final Curso",
				"TFC",
				"programa1",
				_cursoExecucao1,
				new Double(10.0),
				new Double(5.0),
				new Double(3.0),
				new Double(2.0));
		_disciplinaExecucao2 =
			new DisciplinaExecucao(
				"Trabalho Final Curso2",
				"TFC2",
				"programa10",
				_cursoExecucao1,
				new Double(11.0),
				new Double(6.0),
				new Double(4.0),
				new Double(3.0));

		_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(
			_disciplinaExecucao2);

		List list = new ArrayList();
		list.add(_disciplinaCurricular1);

		_disciplinaExecucao1.setAssociatedCurricularCourses(list);

		_disciplinaExecucaoPersistente.escreverDisciplinaExecucao(
			_disciplinaExecucao1);

		TipoAula tipoAula = new TipoAula(TipoAula.TEORICA);
		_turno1 =
			new Turno(
				"turno1",
				tipoAula,
				new Integer(100),
				_disciplinaExecucao1);
		_turnoPersistente.lockWrite(_turno1);
		HashSet privilegios = new HashSet();
		_pessoa1 = new Pessoa();
		_pessoa1.setNumeroDocumentoIdentificacao("0123456789");
		_pessoa1.setCodigoFiscal("9876543210");
		_pessoa1.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_pessoa1.setUsername("user");
		_pessoa1.setPassword("pass");
		privilegios.add(new Privilegio(_pessoa1, new String("CriarTurno")));
		privilegios.add(
			new Privilegio(
				_pessoa1,
				new String("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular")));
		_pessoa1.setPrivilegios(privilegios);
		_pessoaPersistente.escreverPessoa(_pessoa1);

		_suportePersistente.confirmarTransaccao();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public CriarTurnoFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulCriarTurno() {

		// define mapping de origem
		setRequestPathInfo("sop", "/criarTurnoForm");

		// Preenche campos do formulario
		addRequestParameter("nome", "turnoNovo");
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFC");
		addRequestParameter("lotacao", (new Integer(100).toString()));

		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarTurno");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		try {
			InfoDegree iL = new InfoDegree("LEIC", "Informatica");
			InfoExecutionDegree iLE = new InfoExecutionDegree("2002/03", iL);
			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(SessionConstants.INFO_LIC_EXEC_KEY, iLE);

			CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE =
				new CurricularYearAndSemesterAndInfoExecutionDegree(
					new Integer(5),
					new Integer(1),
					iLE);
			getSession().setAttribute(SessionConstants.CONTEXT_KEY, aCSiLE);

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		// invoca acaoo
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucesso");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

	public void testUnsuccessfulCriarTurno() {
		setRequestPathInfo("sop", "/criarTurnoForm");
		addRequestParameter("nome", "turno1");
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFC");
		addRequestParameter("lotacao", (new Integer(100).toString()));

		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarTurno");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		try {
			InfoDegree iL = new InfoDegree("LEIC", "Informatica");
			InfoExecutionDegree iLE = new InfoExecutionDegree("2002/03", iL);
			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(SessionConstants.INFO_LIC_EXEC_KEY, iLE);

			CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE =
				new CurricularYearAndSemesterAndInfoExecutionDegree(
					new Integer(5),
					new Integer(1),
					iLE);
			getSession().setAttribute(SessionConstants.CONTEXT_KEY, aCSiLE);

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute("infoDisciplinasExecucao", iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		actionPerform();
		verifyForwardPath("/naoExecutado.do");

		verifyActionErrors(
			new String[] { "ServidorAplicacao.NotExecutedException" });
	}

	protected void ligarSuportePersistente() {
		try {
			_suportePersistente = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when opening database");
		}
		_turnoPersistente = _suportePersistente.getITurnoPersistente();
		_pessoaPersistente = _suportePersistente.getIPessoaPersistente();
		_cursoExecucaoPersistente =
			_suportePersistente.getICursoExecucaoPersistente();
		_cursoPersistente = _suportePersistente.getICursoPersistente();
		_disciplinaCurricularPersistente =
			_suportePersistente.getIPersistentCurricularCourse();
		_disciplinaExecucaoPersistente =
			_suportePersistente.getIDisciplinaExecucaoPersistente();
		_persistentDepartmentCourse =
			_suportePersistente.getIDisciplinaDepartamentoPersistente();
		_persistentDegreeCurricularPlan =
			_suportePersistente.getIPlanoCurricularCursoPersistente();
		_departamentoPersistente = _suportePersistente.getIDepartamentoPersistente();
		_disciplinaDepartamentoPersistente = _suportePersistente.getIDisciplinaDepartamentoPersistente();
	}

	protected void cleanData() {
		try {
			_suportePersistente.iniciarTransaccao();
			_turnoPersistente.deleteAll();
			_cursoExecucaoPersistente.deleteAll();
			_cursoPersistente.deleteAll();
			_disciplinaCurricularPersistente.deleteAllCurricularCourse();
			_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
			_pessoaPersistente.apagarTodasAsPessoas();
			_persistentDepartmentCourse.apagarTodasAsDisciplinasDepartamento();
			_persistentDegreeCurricularPlan.apagarTodosOsPlanosCurriculares();
			_disciplinaDepartamentoPersistente.apagarTodasAsDisciplinasDepartamento();
			_departamentoPersistente.apagarTodosOsDepartamentos();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}
}