package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
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
@author tfc130
*/
public class EditarAulaFormActionTest extends MockStrutsTestCase {
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
	protected IAula _aula2 = null;
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
		TestSuite suite = new TestSuite(EditarAulaFormActionTest.class);

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
			new PlanoCurricularCurso("plano1", _curso1);

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

		_aula2 =
			new Aula(
				new DiaSemana(DiaSemana.TERCA_FEIRA),
				inicio,
				fim,
				new TipoAula(TipoAula.TEORICA),
				_sala1,
				_disciplinaExecucao1);
		_aulaPersistente.lockWrite(_aula2);

		HashSet privilegios = new HashSet();
		_pessoa1 = new Pessoa();
		_pessoa1.setNumeroDocumentoIdentificacao("0123456789");
		_pessoa1.setCodigoFiscal("9876543210");
		_pessoa1.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		_pessoa1.setUsername("user");
		_pessoa1.setPassword("pass");
		privilegios.add(new Privilegio(_pessoa1, new String("EditarAula")));
		privilegios.add(
			new Privilegio(
				_pessoa1,
				new String("LerAulasDeDisciplinaExecucao")));
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

	public EditarAulaFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulEditarAula() {
		// define mapping de origem
		setRequestPathInfo("", "/editarAulaForm");
		// Preenche campos do formulario
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.TERCA_FEIRA).toString());
		addRequestParameter(
			"horaInicio",
			new Integer(_aula1.getInicio().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosInicio",
			new Integer(_aula1.getInicio().get(Calendar.MINUTE)).toString());
		addRequestParameter(
			"horaFim",
			new Integer(_aula1.getFim().get(Calendar.HOUR_OF_DAY)).toString());
		addRequestParameter(
			"minutosFim",
			new Integer(_aula1.getFim().get(Calendar.MINUTE)).toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter(
			"courseInitials",
			_aula1.getDisciplinaExecucao().getSigla());
		addRequestParameter("nomeSala", "sala1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarAula");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
		try {
			GestorServicos gestor = GestorServicos.manager();
			InfoExecutionCourse iDE =
				new InfoExecutionCourse(
					_disciplinaExecucao1.getNome(),
					_disciplinaExecucao1.getSigla(),
					_disciplinaExecucao1.getPrograma(),
					new InfoExecutionDegree(
						_disciplinaExecucao1
							.getLicenciaturaExecucao()
							.getAnoLectivo(),
						new InfoDegree(
							_disciplinaExecucao1
								.getLicenciaturaExecucao()
								.getCurso()
								.getSigla(),
							_disciplinaExecucao1
								.getLicenciaturaExecucao()
								.getCurso()
								.getSigla())),
					_disciplinaExecucao1.getTheoreticalHours(),
					_disciplinaExecucao1.getPraticalHours(),
					_disciplinaExecucao1.getTheoPratHours(),
					_disciplinaExecucao1.getLabHours());

			getSession().setAttribute("infoDisciplinaExecucao", iDE);
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			getSession().setAttribute("listaAulas", infoAulas);
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			getSession().setAttribute("infoAula", infoAula);

		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		// invoca acção
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucesso");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}
	public void testUnsuccessfulEditarAula() {
		setRequestPathInfo("", "/editarAulaForm");
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter(
			"horaInicio",
			new Integer(_aula1.getInicio().get(Calendar.HOUR_OF_DAY))
				.toString());
		addRequestParameter(
			"minutosInicio",
			new Integer(_aula1.getInicio().get(Calendar.MINUTE)).toString());
		addRequestParameter(
			"horaFim",
			new Integer(_aula1.getFim().get(Calendar.HOUR_OF_DAY)).toString());
		addRequestParameter(
			"minutosFim",
			new Integer(_aula1.getFim().get(Calendar.MINUTE)).toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter(
			"courseInitials",
			_aula1.getDisciplinaExecucao().getSigla());
		addRequestParameter("nomeSala", "sala1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("EditarAula");
		privilegios.add("LerAulasDeDisciplinaExecucao");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
		try {
			GestorServicos gestor = GestorServicos.manager();
			InfoExecutionCourse iDE =
				new InfoExecutionCourse(
					_disciplinaExecucao1.getNome(),
					_disciplinaExecucao1.getSigla(),
					_disciplinaExecucao1.getPrograma(),
					new InfoExecutionDegree(
						_disciplinaExecucao1
							.getLicenciaturaExecucao()
							.getAnoLectivo(),
						new InfoDegree(
							_disciplinaExecucao1
								.getLicenciaturaExecucao()
								.getCurso()
								.getSigla(),
							_disciplinaExecucao1
								.getLicenciaturaExecucao()
								.getCurso()
								.getSigla())),
					_disciplinaExecucao1.getTheoreticalHours(),
					_disciplinaExecucao1.getPraticalHours(),
					_disciplinaExecucao1.getTheoPratHours(),
					_disciplinaExecucao1.getLabHours());
			getSession().setAttribute("infoDisciplinaExecucao", iDE);
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);
			getSession().setAttribute("listaAulas", infoAulas);
			InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
			getSession().setAttribute("infoAula", infoAula);

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