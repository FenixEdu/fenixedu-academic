package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import Dominio.Aula;
import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.Departamento;
import Dominio.DisciplinaDepartamento;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.ISala;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Privilegio;
import Dominio.Sala;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDepartamentoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
import Util.TipoSala;
/**
 * @author tfc130
 */
public class CriarAulaFormActionTest extends MockStrutsTestCase {
	protected ISuportePersistente _suportePersistente = null;
	protected IAulaPersistente _aulaPersistente = null;
	protected ISalaPersistente _salaPersistente = null;
	protected IPersistentCurricularCourse _disciplinaCurricularPersistente =
		null;
	protected IDisciplinaExecucaoPersistente _disciplinaExecucaoPersistente =
		null;

	protected ICursoExecucaoPersistente _cursoExecucaoPersistente = null;
	protected ICursoPersistente _cursoPersistente = null;
	protected IPessoaPersistente _pessoaPersistente = null;
	protected IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan =
		null;
	protected IDisciplinaDepartamentoPersistente persistentDepartmentCourse =
		null;
	protected IDepartamentoPersistente _departamentoPersistente = null;
	protected IDisciplinaDepartamentoPersistente _disciplinaDepartamentoPersistente =
		null;

	protected DiaSemana _diaSemana1 = null;
	protected ISala _sala1 = null;
	protected IAula _aula1 = null;
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
		TestSuite suite = new TestSuite(CriarAulaFormActionTest.class);

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

		IDepartamento departamento = null;
		departamento =
			new Departamento("Departamento de Engenharia Informatica", "DEI");
		IDisciplinaDepartamento departmentCourse = null;
		IPlanoCurricularCurso degreeCurricularPlan = null;

		departmentCourse =
			new DisciplinaDepartamento(
				"Engenharia da Programacao",
				"ep",
				departamento);
		degreeCurricularPlan =
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

		_sala1 =
			new Sala(
				"sala1",
				"edificio",
				new Integer(1),
				new TipoSala(TipoSala.ANFITEATRO),
				new Integer(100),
				new Integer(50));
		_salaPersistente.lockWrite(_sala1);

		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);
		_aula1 =
			new Aula(
				new DiaSemana(DiaSemana.SEGUNDA_FEIRA),
				inicio,
				fim,
				new TipoAula(TipoAula.TEORICA),
				_sala1,
				_disciplinaExecucao1);
		_aulaPersistente.lockWrite(_aula1);

		HashSet privilegios = new HashSet();
		_pessoa1 = new Pessoa();
		_pessoa1.setNumeroDocumentoIdentificacao("0123456789");
		_pessoa1.setCodigoFiscal("9876543210");
		_pessoa1.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_pessoa1.setUsername("user");
		_pessoa1.setPassword("pass");
		privilegios.add(new Privilegio(_pessoa1, new String("CriarAula")));
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

	public CriarAulaFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulCriarAula() {
		// define mapping de origem
		setRequestPathInfo("", "/criarAulaForm");
		// Preenche campos do formulario
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.TERCA_FEIRA).toString());
		addRequestParameter(
			"horaInicio",
			new Integer(((IAula) _aula1).getInicio().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosInicio",
			new Integer(((IAula) _aula1).getInicio().get(Calendar.MINUTE))
				.toString());
		addRequestParameter(
			"horaFim",
			new Integer(((IAula) _aula1).getFim().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosFim",
			new Integer(((IAula) _aula1).getFim().get(Calendar.MINUTE))
				.toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter(
			"courseInitials",
			_aula1.getDisciplinaExecucao().getSigla());
		addRequestParameter("nomeSala", "sala1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarAula");
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

		// invoca acaoo
		actionPerform();

		//verifica ausencia de erros
		verifyNoActionErrors();

		// verifica reencaminhamento
		verifyForward("Sucesso");
	}

	public void testUnsuccessfulCriarAula() {
		setRequestPathInfo("", "/criarAulaForm");
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter(
			"horaInicio",
			new Integer(((IAula) _aula1).getInicio().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosInicio",
			new Integer(((IAula) _aula1).getInicio().get(Calendar.MINUTE))
				.toString());
		addRequestParameter(
			"horaFim",
			new Integer(((IAula) _aula1).getFim().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosFim",
			new Integer(((IAula) _aula1).getFim().get(Calendar.MINUTE))
				.toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter(
			"courseInitials",
			_aula1.getDisciplinaExecucao().getSigla());
		addRequestParameter("nomeSala", "sala1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarAula");
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
		_aulaPersistente = _suportePersistente.getIAulaPersistente();
		_salaPersistente = _suportePersistente.getISalaPersistente();
		_pessoaPersistente = _suportePersistente.getIPessoaPersistente();
		_cursoExecucaoPersistente =
			_suportePersistente.getICursoExecucaoPersistente();
		_cursoPersistente = _suportePersistente.getICursoPersistente();
		_disciplinaCurricularPersistente =
			_suportePersistente.getIPersistentCurricularCourse();
		_disciplinaExecucaoPersistente =
			_suportePersistente.getIDisciplinaExecucaoPersistente();
		persistentDepartmentCourse =
			_suportePersistente.getIDisciplinaDepartamentoPersistente();
		persistentDegreeCurricularPlan =
			_suportePersistente.getIPlanoCurricularCursoPersistente();
		_disciplinaDepartamentoPersistente =
			_suportePersistente.getIDisciplinaDepartamentoPersistente();
		_departamentoPersistente =
			_suportePersistente.getIDepartamentoPersistente();
	}

	protected void cleanData() {
		try {
			_suportePersistente.iniciarTransaccao();
			_aulaPersistente.deleteAll();
			_salaPersistente.deleteAll();
			_cursoExecucaoPersistente.deleteAll();
			_cursoPersistente.deleteAll();
			_disciplinaCurricularPersistente.deleteAllCurricularCourse();
			_disciplinaExecucaoPersistente.apagarTodasAsDisciplinasExecucao();
			_pessoaPersistente.apagarTodasAsPessoas();
			persistentDepartmentCourse.apagarTodasAsDisciplinasDepartamento();
			persistentDegreeCurricularPlan.apagarTodosOsPlanosCurriculares();
			_departamentoPersistente.apagarTodosOsDepartamentos();
			_disciplinaDepartamentoPersistente
				.apagarTodasAsDisciplinasDepartamento();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when cleaning data");
		}
	}
}